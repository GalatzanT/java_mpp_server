package networking.jsonprotocol;


import networking.dto.ClientDTO;
import networking.dto.MatchDTO;
import networking.dto.TicketDTO;
import networking.dto.WorkerDTO;

import java.util.Arrays;

public class Request {
    private RequestType type;
    private WorkerDTO workerDTO;
    private ClientDTO clientDTO;
    private MatchDTO matchDTO;
    private TicketDTO ticketDTO;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Request(){}
    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public WorkerDTO getWorkerDTO() {
        return workerDTO;
    }

    public void setWorkerDTO(WorkerDTO workerDTO) {
        this.workerDTO = workerDTO;
    }

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
    }

    public MatchDTO getMatchDTO() {
        return matchDTO;
    }

    public void setMatchDTO(MatchDTO matchDTO) {
        this.matchDTO = matchDTO;
    }

    public TicketDTO getTicketDTO() {
        return ticketDTO;
    }

    public void setTicketDTO(TicketDTO ticketDTO) {
        this.ticketDTO = ticketDTO;
    }


    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", workerDTO=" + workerDTO +
                ", clientDTO=" + clientDTO +
                ", matchDTO=" + matchDTO +
                ", ticketDTO=" + ticketDTO +
                '}';
    }
}
