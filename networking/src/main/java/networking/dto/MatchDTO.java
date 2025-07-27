package networking.dto;

import java.io.Serializable;

public class MatchDTO implements Serializable {

        private Integer id;
        private String team1;
        private String team2;
        private String matchType;
        private int ticketPrice;
        private int availableSeats;

        public MatchDTO(Integer id, String team1, String team2, String matchType, int ticketPrice, int availableSeats) {
            this.id = id;
            this.team1 = team1;
            this.team2 = team2;
            this.matchType = matchType;
            this.ticketPrice = ticketPrice;
            this.availableSeats = availableSeats;
        }

        // Getteri și setteri

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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
        if(availableSeats > 0){
            return  team1 + " vs "+ team2 + " type: "+  matchType  +
                    " ticket price: " + ticketPrice +
                    " available seats: " + availableSeats;
        }
        else {
            return  team1 + " vs "+ team2 + " type: " +matchType  +
                    " ticket price: " + ticketPrice;
        }

    }
    }

