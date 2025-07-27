package model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;


@Entity
@Table(name = "Matches")
public class Match extends EntityBase<Integer> {

    @Column(name = "team1", nullable = false)
    private String team1;

    @Column(name = "team2", nullable = false)
    private String team2;

    @Column(name = "matchType", nullable = false)
    private String matchType;

    @Column(name = "ticketPrice", nullable = false)
    private int ticketPrice;

    @Column(name = "availableSeats", nullable = false)
    private int availableSeats;

    public Match(String team1, String team2, String matchType, int ticketPrice, int availableSeats) {
        this.team1 = team1;
        this.team2 = team2;
        this.matchType = matchType;
        this.ticketPrice = ticketPrice;
        this.availableSeats = availableSeats;
    }
    public Match(Integer id, String team1, String team2, String matchType, int ticketPrice, int availableSeats) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.matchType = matchType;
        this.ticketPrice = ticketPrice;
        this.availableSeats = availableSeats;
    }
    public Match() {
        // Default constructor
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    @Override
    public String toString() {
        if (availableSeats > 0) {
            return team1 + " vs " + team2 + " type: " + matchType +
                    " ticket price: " + ticketPrice +
                    " available seats: " + availableSeats;
        } else {
            return team1 + " vs " + team2 + " type: " + matchType +
                    " ticket price: " + ticketPrice;
        }
    }

// Getters and Setters
}

//
//public class Match extends Entity<Integer>{
//    String team1;
//    String team2;
//    String matchType;
//    int ticketPrice;
//    int availableSeats;
//
//    public Match(String team1, String team2, String matchType, int ticketPrice, int availableSeats) {
//        this.team1 = team1;
//        this.team2 = team2;
//        this.matchType = matchType;
//        this.ticketPrice = ticketPrice;
//        this.availableSeats = availableSeats;
//    }
//
//    public String getTeam1() {
//        return team1;
//    }
//
//    @Override
//    public String toString() {
//        if(availableSeats > 0){
//            return  team1 + " vs "+ team2 + " type: "+  matchType  +
//                    " ticket price: " + ticketPrice +
//                    " available seats: " + availableSeats;
//        }
//        else {
//            return  team1 + " vs "+ team2 + " type: " +matchType  +
//                    " ticket price: " + ticketPrice;
//        }
//
//    }
//
//    public void setTeam1(String team1) {
//        this.team1 = team1;
//    }
//
//    public String getTeam2() {
//        return team2;
//    }
//
//    public void setTeam2(String team2) {
//        this.team2 = team2;
//    }
//
//    public int getTicketPrice() {
//        return ticketPrice;
//    }
//
//    public void setTicketPrice(int ticketPrice) {
//        this.ticketPrice = ticketPrice;
//    }
//
//    public String getMatchType() {
//        return matchType;
//    }
//
//    public void setMatchType(String matchType) {
//        this.matchType = matchType;
//    }
//
//    public int getAvailableSeats() {
//        return availableSeats;
//    }
//
//    public void setAvailableSeats(int availableSeats) {
//        this.availableSeats = availableSeats;
//    }
//}
