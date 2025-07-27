package networking.jsonprotocol;


import model.Ticket;
import networking.dto.DTOUtils;

import services.IObserver;
import com.google.gson.Gson;
import model.Worker;
import networking.dto.WorkerDTO;
import networking.utils.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.BasketException;
import services.IObserver;
import services.IServices;
import networking.utils.TextUtils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientJsonWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;

    private static Logger logger = LogManager.getLogger(ChatClientJsonWorker.class);

    public ChatClientJsonWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter=new Gson();
        try{
            output=new PrintWriter(connection.getOutputStream());
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected=true;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    public void run() {
        while(connected){
            try {
                String requestLine=input.readLine();
                Request request=gsonFormatter.fromJson(requestLine, Request.class);
                Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error "+e);
        }
    }

    private static Response okResponse=JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request){
       Response response=null;
        if (request.getType()== RequestType.LOGIN){
            logger.debug("Login request ...{}"+request.getWorkerDTO());
            WorkerDTO udto=request.getWorkerDTO();
            Worker user=DTOUtils.getFromDTO(udto);
            user.setPassword(user.getPassword());
            try {
                server.login(user, this);
                return okResponse;
            } catch (BasketException e) {
                connected=false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.LOGOUT){
            logger.debug("Logout request {}",request.getWorkerDTO());
            WorkerDTO udto=request.getWorkerDTO();
            Worker user=DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected=false;
                return okResponse;

            } catch (BasketException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.GET_MATCHES) {
            logger.debug("GetMatches request received");
            try {
                var matches = server.getMatches(); // obține meciurile de la service
                var matchDTOs = DTOUtils.getDTO(matches); // convertire în DTO
                response = new Response();
                response.setType(ResponseType.MATCHES_RECIEVED);
                response.setMatchDTOs(matchDTOs); // setează lista de DTO
                return response;
            } catch (BasketException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.TICKET_ORDER) {
            logger.debug("BuyTicket request received");
            try {
                // Convertește DTO în obiect model
                Ticket ticket = DTOUtils.getFromDTO(request.getTicketDTO());

                // Apelează service pentru a cumpăra biletul
                server.buyTicket(ticket);

                // Poți notifica alți observatori despre schimbare, dacă vrei

                return okResponse;  // răspuns OK către client
            } catch (BasketException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.SEARCH_NAME) {
            logger.debug("Search tickets by client name request received");
            try {
                String name = request.getData();
                var tickets = server.findTicketsByClientName(name);
                var ticketDTOs = tickets.stream()
                        .map(DTOUtils::getDTO)
                        .toArray(networking.dto.TicketDTO[]::new);
                response = new Response();
                response.setType(ResponseType.OK);
                response.setTicketDTOs(ticketDTOs);
                return response;
            } catch (BasketException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.SEARCH_ADDRESS) {
            logger.debug("Search tickets by client name request received");
            try {
                String name = request.getData();
                var tickets = server.findTicketsByClientAddress(name);
                var ticketDTOs = tickets.stream()
                        .map(DTOUtils::getDTO)
                        .toArray(networking.dto.TicketDTO[]::new);
                response = new Response();
                response.setType(ResponseType.OK);
                response.setTicketDTOs(ticketDTOs);
                return response;
            } catch (BasketException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        ///may be usefull later
        /*
        if (request.getType()== RequestType.SEND_MESSAGE){

            MessageDTO mdto=(MessageDTO)request.getMessage();
            Message message=DTOUtils.getFromDTO(mdto);
            logger.debug("SendMessageRequest ...{} ",message);
            try {
                server.sendMessage(message);
                return okResponse;
            } catch (ChatException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if (request.getType()== RequestType.GET_LOGGED_FRIENDS){
            logger.debug("GetLoggedFriends Request ...user= {}",request.getUser());
            UserDTO udto=request.getUser();
            User user=DTOUtils.getFromDTO(udto);
            try {
                User[] friends=server.getLoggedFriends(user);

                return JsonProtocolUtils.createGetLoggedFriendsResponse(friends);
            } catch (ChatException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

         */
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        String responseLine=gsonFormatter.toJson(response);
        logger.debug("sending response "+responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }

    @Override
    public void TicketOrder(Ticket ticket) throws BasketException {
        Response response = new Response();
        response.setType(ResponseType.NEW_TICKET_BOUGHT);
        response.setTicketDTO(DTOUtils.getDTO(ticket));

        try {
            sendResponse(response);
        } catch (IOException e) {
            throw new BasketException("Error sending ticket order notification: " + e.getMessage());
        }
    }
}
