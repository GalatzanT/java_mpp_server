package server;

import model.Client;
import model.Match;
import model.Ticket;
import model.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.ClientRepo;
import persistence.MatchRepo;
import persistence.TicketRepo;
import persistence.WorkerRepo;
import services.IObserver;
import services.IServices;
import services.BasketException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceImpl implements IServices {
    private ClientRepo clientRepo;
    private MatchRepo matchRepo;
    private TicketRepo ticketRepo;
    private WorkerRepo workerRepo;

    //tinem minte clientii ce intra pe server
    private Map<String, IObserver> loggedClients;

    private static Logger logger = LogManager.getLogger(ServiceImpl.class);

    public ServiceImpl(ClientRepo clientRepo, MatchRepo matchRepo, TicketRepo ticketRepo, WorkerRepo workerRepo) {
        this.clientRepo = clientRepo;
        this.matchRepo = matchRepo;
        this.ticketRepo = ticketRepo;
        this.workerRepo = workerRepo;

        loggedClients = new ConcurrentHashMap<>();
    }



    @Override
    public synchronized void login(Worker worker, IObserver client) throws BasketException {
        System.out.println(worker.getUsername() + " " + worker.getPassword());
        Worker workerR = workerRepo.findBy(worker.getUsername(), worker.getPassword());
        if(workerR != null) {
            if(loggedClients.get(workerR.getUsername()) != null) {
                throw new BasketException("User already logged in.");
            }
            loggedClients.put(workerR.getUsername(), client);
        } else {
            throw new BasketException("Authentication failed.");
        }
    }


    @Override
    public synchronized void logout(Worker worker, IObserver client) throws BasketException {
        IObserver localClient = loggedClients.remove(worker.getUsername());
        if(localClient==null) {
            throw new BasketException("User " + worker.getUsername() + " is not logged in.");
        }
    }

    @Override
    public synchronized List<Match> getMatches() throws BasketException {
        return matchRepo.findAll();

    }

    @Override
    public void buyTicket(Ticket ticket) throws BasketException {

        Match matchFromDb = matchRepo.findOne(ticket.getMatch().getId());
        if(matchFromDb == null) {
            throw new BasketException("Match not found.");
        }

        int availableSeats = matchFromDb.getAvailableSeats();
        System.out.println("Available seats from DB: " + availableSeats);

        if(availableSeats <= 0) {
            throw new BasketException("No available seats for this match.");
        }
        if(availableSeats < ticket.getNumberOfSeats()) {
            throw new BasketException("Not enough available seats for this match.");
        }

        // Setează meciul actualizat în ticket, ca să fie sincron
        ticket.setMatch(matchFromDb);

        // Verifică clientul în baza de date
        Client existingClient = clientRepo.findBy(ticket.getClient().getName(), ticket.getClient().getAddres());
        if(existingClient == null){
            // Clientul e nou, salvează-l și setează-l în ticket
            Client savedClient = clientRepo.save(ticket.getClient());
            if(savedClient == null){
                throw new BasketException("Could not save client.");
            }
            ticket.setClient(savedClient);
        } else {
            // Clientul există deja, îl setezi direct în ticket
            ticket.setClient(existingClient);
        }

        // Acum salvezi biletul, clientul are id valid
        ticketRepo.save(ticket);

        // Scade locurile disponibile
        matchFromDb.setAvailableSeats(availableSeats - ticket.getNumberOfSeats());

        // Actualizează meciul în baza de date
        matchRepo.update(matchFromDb);

        notifyObserversAboutTicketOrder(ticket);
    }




    private void notifyObserversAboutTicketOrder(Ticket ticket) {
        System.out.println("Notifying observer about ticket: " + ticket);

        for (IObserver obs : loggedClients.values()) {
            try {
                obs.TicketOrder(ticket);
            } catch (BasketException e) {
                // Poți loga eroarea și eventual elimina observerul nefuncțional
                logger.error("Error notifying observer: " + e.getMessage());
            }
        }
    }




    @Override
    public List<Ticket> findTicketsByClientName(String name) throws BasketException {
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : ticketRepo.findAll()) {
            if (ticket.getClient().getName().equals(name)) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    @Override
    public List<Ticket> findTicketsByClientAddress(String address) throws BasketException {
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : ticketRepo.findAll()) {
            if (ticket.getClient().getAddres().equals(address)) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }
}
