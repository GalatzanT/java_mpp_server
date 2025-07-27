package services;

import model.Ticket;
import model.Worker;
import  model.Match;
import java.util.List;

public interface IServices {
    void login(Worker worker, IObserver client) throws BasketException;

    void logout(Worker worker, IObserver client) throws BasketException;

    List<Match> getMatches() throws BasketException;

    void buyTicket(Ticket ticket) throws BasketException;

    List<Ticket> findTicketsByClientName(String name) throws BasketException;

    List<Ticket> findTicketsByClientAddress(String address) throws BasketException;


}
