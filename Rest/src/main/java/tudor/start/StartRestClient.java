package tudor.start;


import model.Match;
import tudor.service.rest.ServiceException;
import tudor.rest.client.MatchesClient;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.time.LocalDateTime;
import java.util.Arrays;

public class StartRestClient {
    private final static MatchesClient spectacoleClient = new MatchesClient();

    public static void main(String[] args) {
        try {
            System.out.println("=========== GET ALL ===========");
            show(() -> {
                Match[] res = spectacoleClient.getAll();
                for (Match s : res) {
                    System.out.println("id: " + s.getId() + " " + s.getTeam1() + " vs " + s.getTeam2() +" "+s.getMatchType()+ " Seats: " +  s.getAvailableSeats() + " Price:  " + s.getTicketPrice());
                }
            });


            System.out.println("\n=========== GET BY ID ===========");
            show(() -> {
                Match gasit = spectacoleClient.getById(1);
                System.out.println("Found match: " + gasit);
            });
            System.out.println("\n=========== ADD NEW MATCH ===========");
            Match meciNou = new Match(30, "a", "b", "Finala1", 120, 1000);
            spectacoleClient.create(meciNou);

            System.out.println("Added match: " + meciNou);

            show(() -> {
                Match[] res = spectacoleClient.getAll();
                for (Match s : res) {
                    System.out.println("id: " + s.getId() + " " + s.getTeam1() + " vs " + s.getTeam2() +s.getMatchType()+ " Seats: " +  s.getAvailableSeats() + " Price:  " + s.getTicketPrice());
                }
            });


            System.out.println("\n=========== UPDATE MATCH ===========");
            meciNou.setAvailableSeats(750);
            meciNou.setTicketPrice(150);
            meciNou.setMatchType("AAA");
            meciNou.setTeam1("c");
            meciNou.setTeam2("d");

            spectacoleClient.update(meciNou);
            show(() -> {
                Match[] res = spectacoleClient.getAll();
                for (Match s : res) {
                    System.out.println("id: " + s.getId() + " " + s.getTeam1() + " vs " + s.getTeam2() +s.getMatchType()+ " Seats: " +  s.getAvailableSeats() + " Price:  " + s.getTicketPrice());
                }
            });



            System.out.println("\n=========== DELETE MATCH ===========");

                spectacoleClient.delete(meciNou.getId());

            show(() -> {
                Match[] res = spectacoleClient.getAll();
                for (Match s : res) {
                    System.out.println("id: " + s.getId() + " " + s.getTeam1() + " vs " + s.getTeam2() +s.getMatchType()+ " Seats: " +  s.getAvailableSeats() + " Price:  " + s.getTicketPrice());
                }
            });



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
