package networking.jsonprotocol;

import model.Match;
import networking.dto.ClientDTO;
import networking.dto.MatchDTO;
import networking.dto.TicketDTO;
import networking.dto.WorkerDTO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class Response implements Serializable {
    private ResponseType type;
    private String errorMessage;
    private WorkerDTO workerDTO;
    private ClientDTO clientDTO;
    private MatchDTO matchDTO;
    private MatchDTO[] matchDTOs; // 🔥 AICI!
    private TicketDTO ticketDTO;
    private TicketDTO[] ticketDTOs;




    public TicketDTO[] getTicketDTOs() {
        return ticketDTOs;
    }

    public void setTicketDTOs(TicketDTO[] ticketDTOs) {
        this.ticketDTOs = ticketDTOs;
    }


    public ResponseType getType() { return type; }
    public void setType(ResponseType type) { this.type = type; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public WorkerDTO getWorkerDTO() { return workerDTO; }
    public void setWorkerDTO(WorkerDTO workerDTO) { this.workerDTO = workerDTO; }

    public ClientDTO getClientDTO() { return clientDTO; }
    public void setClientDTO(ClientDTO clientDTO) { this.clientDTO = clientDTO; }

    public MatchDTO getMatchDTO() { return matchDTO; }
    public void setMatchDTO(MatchDTO matchDTO) { this.matchDTO = matchDTO; }

    public MatchDTO[] getMatchDTOs() { return matchDTOs; } // 🔥 Getter nou
    public void setMatchDTOs(MatchDTO[] matchDTOs) { this.matchDTOs = matchDTOs; } // 🔥 Setter nou

    public TicketDTO getTicketDTO() { return ticketDTO; }
    public void setTicketDTO(TicketDTO ticketDTO) { this.ticketDTO = ticketDTO; }
}
