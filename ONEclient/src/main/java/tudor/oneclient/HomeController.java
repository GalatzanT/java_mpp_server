package tudor.oneclient;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Client;
import model.Match;
import model.Ticket;
import model.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.BasketException;
import services.IObserver;
import services.IServices;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable, IObserver {


    @FXML
    private AnchorPane rootPane; // legat în FXML (root pane-ul ferestrei)
    private Worker worker;
    private IServices server;
    private static Logger logger = LogManager.getLogger(HomeController.class);


    @FXML
    private ListView<Match> matchListView;
    @FXML
    private ObservableList<Match> matches = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @Override
    public void TicketOrder(Ticket ticket) throws BasketException {
        System.out.println("TicketOrder called on client for ticket: " + ticket);

        Platform.runLater(() -> {
            try {
                updateMatches();
            } catch (BasketException e) {
                MessageAlert.showAlert(null, "Error updating matches: " + e.getMessage());
            }
        });
    }

    public void setServer(IServices s) {
        server = s;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void logout() {
        try {
            server.logout(worker, this);
            logger.info("User logged out: " + worker.getUsername());
        } catch (BasketException e) {
            logger.error("Logout error " + e);
        }
        Platform.runLater(() -> {
            // Închidem fereastra curentă
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.close();


            // Other methods and logic for the HomeController class

        });
    }

    public void updateMatches() throws BasketException {
        matches.setAll(server.getMatches());
        matchListView.setItems(matches);
        matchListView.setCellFactory(listView -> new ListCell<Match>() {
            @Override
            protected void updateItem(Match match, boolean empty) {
                super.updateItem(match, empty);
                if (empty || match == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text mainText = new Text(match.toString());
                    mainText.setStyle("-fx-fill: black;");

                    if (match.getAvailableSeats() == 0) {
                        Text soldOutText = new Text(" SOLD OUT");
                        soldOutText.setStyle("-fx-fill: red; -fx-font-weight: bold;");

                        TextFlow flow = new TextFlow(mainText, soldOutText);
                        setGraphic(flow);
                    } else {
                        setGraphic(new TextFlow(mainText));
                    }
                }
            }
        });
    }

    @FXML
    private TextField clientName;

    @FXML
    private TextField clientAddress;

    @FXML
    private TextField numberOfTickets;

    @FXML
    public void handleBuyTickets() {
        Match selectedMatch = matchListView.getSelectionModel().getSelectedItem();

        if (selectedMatch == null) {
            MessageAlert.showAlert(null, "Please select a match.");
            return;
        }

        String name = clientName.getText().trim();
        String address = clientAddress.getText().trim();
        String numberStr = numberOfTickets.getText().trim();

        if (name.isEmpty() || address.isEmpty() || numberStr.isEmpty()) {
            MessageAlert.showAlert(null, "All fields must be filled.");
            return;
        }

        int numberOfSeats;
        try {
            numberOfSeats = Integer.parseInt(numberStr);
        } catch (NumberFormatException e) {
            MessageAlert.showAlert(null, "Invalid number of tickets.");
            return;
        }

        try {
            Ticket t = new Ticket(new Client(name, address), selectedMatch, numberOfSeats);
            server.buyTicket(t);
            MessageAlert.showSucces(null, "Success, Tickets bought successfully!");
            updateMatches();
        } catch (Exception e) {
            MessageAlert.showAlert(null, "Error: " + e.getMessage());
        }


    }
    @FXML
    private TextField searchClientField;

    @FXML
    private ListView<Ticket> ticketListView;
    private ObservableList<Ticket> tickets = FXCollections.observableArrayList();



    @FXML
    public void handleSearchOrders() throws BasketException {
        tickets.clear();
        String name = searchClientField.getText().trim();
        List<Ticket> foundTickets = server.findTicketsByClientName(name);

        if (foundTickets.isEmpty()) {
            MessageAlert.showAlert(null, "No orders found for the client: " + name);
        } else {
            tickets.addAll(foundTickets);
        }
        ticketListView.setItems(tickets);


}

    @FXML
    public void handleSearchOrdersEmail() throws BasketException {
        tickets.clear();
        ticketListView.setItems(tickets);
        String name = searchClientField.getText().trim();
        List<Ticket> tickets1 = server.findTicketsByClientAddress(name);

        if (tickets1.isEmpty()) {
            MessageAlert.showAlert(null, "No orders found for the client adress: " + name);
        } else {
            tickets1.forEach(ticket ->{
                tickets.add(ticket);
            });
            ticketListView.setItems(tickets);
        }
    }








}
