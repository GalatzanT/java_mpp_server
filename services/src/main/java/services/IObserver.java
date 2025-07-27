package services;

import model.Ticket;

public interface IObserver {
    void TicketOrder(Ticket ticket) throws BasketException;



}
