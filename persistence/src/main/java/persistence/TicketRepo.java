package persistence;

import model.Ticket;

import java.util.List;

public interface TicketRepo extends Repo<Integer, Ticket> {
    List<Ticket> findAllByClientId(Integer clientId);
}
