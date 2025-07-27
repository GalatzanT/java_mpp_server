package model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;

@Entity
@Table(name = "Tickets")
public class Ticket extends EntityBase<Integer> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idClient", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER) // Many tickets can be associated with one client
    @JoinColumn(name = "idMatch", nullable = false)
    private Match match;


    @Column(name = "numberOfSeats", nullable = false)
    private int numberOfSeats;

    public Ticket(Client client, Match match, int numberOfSeats) {
        this.client = client;
        this.match = match;
        this.numberOfSeats = numberOfSeats;
    }

    public Ticket() {
        // Default constructor
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return client.getName() + " " + client.getAddres() +
                " match: " + match.getTeam1() + " vs " + match.getTeam2() +
                " number of seats: " + numberOfSeats;
    }

}

//public class Ticket extends Entity<Integer> {
//    Client client;
//    Match match;
//    int numberOfSeats;
//
//    public Ticket(Client client, Match match, int numberOfSeats) {
//        this.client = client;
//        this.match = match;
//        this.numberOfSeats = numberOfSeats;
//    }
//
//    public Client getClient() {
//        return client;
//    }
//
//    public void setClient(Client client) {
//        this.client = client;
//    }
//
//    public Match getMatch() {
//        return match;
//    }
//
//    public void setMatch(Match match) {
//        this.match = match;
//    }
//
//    public int getNumberOfSeats() {
//        return numberOfSeats;
//    }
//
//    public void setNumberOfSeats(int numberOfSeats) {
//        this.numberOfSeats = numberOfSeats;
//    }
//
//    @Override
//    public String toString() {
//        return  client.getName() + " " + client.addres + " match:" + match.team1 + " vs "  + match.team2 +
//                " number of seats: " + numberOfSeats;
//    }
//}
