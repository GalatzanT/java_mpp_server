package networking.jsonprotocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Match;
import model.Ticket;
import model.Worker;
import networking.dto.DTOUtils;
import networking.dto.MatchDTO;
import networking.dto.TicketDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.BasketException;
import services.IObserver;
import services.IServices;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceRPCPRoxy implements IServices {
    private String host;
    private int port;
    private static Logger logger = LogManager.getLogger(ServiceRPCPRoxy.class);
    private Gson gsonFormatter;
    private IObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServiceRPCPRoxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.gsonFormatter = new GsonBuilder().create();
        qresponses = new LinkedBlockingQueue<>();
    }

    private void initializeConnection() throws IOException {
        connection = new Socket(host, port);
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        output = new PrintWriter(connection.getOutputStream());
        finished = false;
        new Thread(new ReaderThread()).start();
    }

    @Override
    public void login(Worker worker, IObserver client) throws  BasketException {
        logger.info("Login started");
        try {
            initializeConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("connection initialized");
        this.client = client;
        Worker encodedWorker = new Worker( worker.getUsername(), worker.getPassword());
        encodedWorker.setId(worker.getId());
        Request request = JsonProtocolUtils.createLoginRequest(encodedWorker);
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            closeConnection();
            throw new BasketException(response.getErrorMessage());
        }
    }

    @Override
    public void logout(Worker worker, IObserver client) throws BasketException {
        Request request = JsonProtocolUtils.createLogoutRequest(worker);
        sendRequest(request);
        Response response = readResponse();
        closeConnection();
        if (response.getType() == ResponseType.ERROR) {
            throw new BasketException(response.getErrorMessage());
        }
    }

    @Override
    public List<Match> getMatches() throws BasketException {
        Request request = JsonProtocolUtils.createGetMatches();
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR)
            throw new BasketException(response.getErrorMessage());
        MatchDTO[] matchDTOs = (MatchDTO[]) response.getMatchDTOs();
        return DTOUtils.getFromDTO(matchDTOs);

    }

    @Override
    public void buyTicket(Ticket ticket) throws BasketException {
        Request request = JsonProtocolUtils.createBuyTicketRequest(ticket);
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR)
            throw new BasketException(response.getErrorMessage());
    }

    @Override
    public List<Ticket> findTicketsByClientName(String name) throws BasketException {
        Request request = JsonProtocolUtils.createFindTicketsByNameRequest(name);
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR)
            throw new BasketException(response.getErrorMessage());
        TicketDTO[] ticketDTOs = response.getTicketDTOs();  // Array, nu listă
        return Arrays.stream(ticketDTOs)
                .map(DTOUtils::getFromDTO)
                .toList();
    }

    @Override
    public List<Ticket> findTicketsByClientAddress(String address) throws BasketException {
        Request request = JsonProtocolUtils.createFindTicketsByAddressRequest(address);
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR)
            throw new BasketException(response.getErrorMessage());
        TicketDTO[] ticketDTOs = response.getTicketDTOs();  // Array, nu listă
        return Arrays.stream(ticketDTOs)
                .map(DTOUtils::getFromDTO)
                .toList();
    }

    private void sendRequest(Request request) throws BasketException {
        String requestLine = gsonFormatter.toJson(request);
        logger.debug("Sending request: " + requestLine);
        synchronized (output) {
            output.println(requestLine);
            output.flush();
        }
    }

    private Response readResponse() throws BasketException {
        try {
            return qresponses.take(); // blocking
        } catch (InterruptedException e) {
            throw new BasketException("Error waiting for response: " + e.getMessage());
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            logger.error("Error closing connection: ", e);
        }
    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.NEW_TICKET_BOUGHT;
    }

    private void handleUpdate(Response response) {
        Ticket ticket = DTOUtils.getFromDTO(response.getTicketDTO());
        try {
            client.TicketOrder(ticket);
        } catch (BasketException e) {
            logger.error("Error handling ticket update", e);
        }
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    String responseLine = input.readLine();
                    logger.debug("Response received: " + responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    if (response == null)
                        continue;
                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        qresponses.put(response);
                    }
                } catch (IOException | InterruptedException e) {
                    logger.error("ReaderThread error", e);
                }
            }
        }
    }
}
