package networking.dto;

import java.io.Serializable;

public class TicketDTO implements Serializable {
    private Integer id;
    private Integer clientId;
    private String clientName;
    private String clientAddress;

    private Integer matchId;
    private String team1;
    private String team2;

    private int numberOfSeats;

    public TicketDTO(Integer id, Integer clientId, String clientName, String clientAddress,
                     Integer matchId, String team1, String team2, int numberOfSeats) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.matchId = matchId;
        this.team1 = team1;
        this.team2 = team2;
        this.numberOfSeats = numberOfSeats;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
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

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", match=" + team1 + " vs " + team2 +
                ", numberOfSeats=" + numberOfSeats +
                '}';
    }
}
