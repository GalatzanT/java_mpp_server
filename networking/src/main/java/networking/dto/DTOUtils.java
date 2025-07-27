package networking.dto;

import model.Client;
import model.Match;
import model.Ticket;
import model.Worker;

import java.util.ArrayList;
import java.util.List;


public class DTOUtils {
    public static Worker getFromDTO(WorkerDTO usdto){
        int id=usdto.getId();

        String pass=usdto.getPass();
        String name=usdto.getName();
        Worker w = new Worker(name, pass) ;
        w.setId(id);
        return w;

    }
    public static WorkerDTO getDTO(Worker worker){
        Integer id=worker.getId();
        int safeId = (id != null) ? id.intValue() : -1;
        String name=worker.getUsername();
        String pass=worker.getPassword();
        return new WorkerDTO(safeId, name, pass);
    }


    public static Match getFromDTO(MatchDTO mdto){
        int id=mdto.getId();
        String team1=mdto.getTeam1();
        String team2=mdto.getTeam2();
        String matchType=mdto.getMatchType();
        int ticketPrice=mdto.getTicketPrice();
        int availableSeats=mdto.getAvailableSeats();
        Match m = new Match(team1, team2, matchType, ticketPrice, availableSeats);
        m.setId(id);
        return m;
    }
    public static MatchDTO getDTO(Match match){
        return new MatchDTO(
                match.getId(),
                match.getTeam1(),
                match.getTeam2(),
                match.getMatchType(),
                match.getTicketPrice(),
                match.getAvailableSeats()
        );
    }

    public static ClientDTO getDTO(Client client) {
        return new ClientDTO(client.getId(), client.getName(), client.getAddres());
    }

    public static Client getFromDTO(ClientDTO dto) {
        Client client = new Client(dto.getName(), dto.getAddres());
        client.setId(dto.getId());
        return client;
    }

    public static TicketDTO getDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getClient().getId(),
                ticket.getClient().getName(),
                ticket.getClient().getAddres(),
                ticket.getMatch().getId(),
                ticket.getMatch().getTeam1(),
                ticket.getMatch().getTeam2(),
                ticket.getNumberOfSeats()
        );
    }

    public static Ticket getFromDTO(TicketDTO dto) {
        Client client = new Client(dto.getClientName(), dto.getClientAddress());
        client.setId(dto.getClientId());

        Match match = new Match(dto.getTeam1(), dto.getTeam2(), "", 0, 0);
        match.setId(dto.getMatchId());

        Ticket ticket = new Ticket(client, match, dto.getNumberOfSeats());
        ticket.setId(dto.getId());

        return ticket;
    }

    public static List<Match> getFromDTO(MatchDTO[] matchDTOs) {
        List<Match> matches = new ArrayList<>();
        for (MatchDTO dto : matchDTOs) {
            matches.add(getFromDTO(dto));
        }
        return matches;
    }
    public static MatchDTO[] getDTO(List<Match> matches) {
        MatchDTO[] matchDTOs = new MatchDTO[matches.size()];
        for (int i = 0; i < matches.size(); i++) {
            matchDTOs[i] = getDTO(matches.get(i));  // ai nevoie de metoda getDTO(Match)
        }
        return matchDTOs;
    }

    public static TicketDTO[] getDTOTickets(List<Ticket> tickets) {
        TicketDTO[] ticketDTOs = new TicketDTO[tickets.size()];
        for (int i = 0; i < tickets.size(); i++) {
            ticketDTOs[i] = getDTO(tickets.get(i));
        }
        return ticketDTOs;
    }

    public static List<Ticket> getFromDTOTickets(TicketDTO[] dtos) {
        List<Ticket> tickets = new ArrayList<>();
        for (TicketDTO dto : dtos) {
            tickets.add(getFromDTO(dto));
        }
        return tickets;
    }


}
