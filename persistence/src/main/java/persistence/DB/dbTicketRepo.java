package persistence.DB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.Client;
import model.Match;
import model.Ticket;
import persistence.ClientRepo;
import persistence.MatchRepo;
import persistence.TicketRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class dbTicketRepo implements TicketRepo {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    private ClientRepo clientRepo;
    private MatchRepo matchRepo;

    public dbTicketRepo(Properties props, ClientRepo clientRepo, MatchRepo matchRepo) {
        logger.info("Initializing dbTicketRepo with properties: {}", props);
        this.dbUtils = new JdbcUtils(props);
        this.clientRepo = clientRepo;
        this.matchRepo = matchRepo;
    }

    @Override
    public Ticket findOne(Integer id) {
        logger.info("Finding ticket with id: {}", id);
        String query = "SELECT * FROM Tickets WHERE id = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return buildTicketFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding ticket", e);
        }
        return null;
    }

    @Override
    public List<Ticket> findAll() {
        logger.info("Finding all tickets");
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Tickets";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                tickets.add(buildTicketFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all tickets", e);
        }
        return tickets;
    }

    @Override
    public Ticket save(Ticket ticket) {
        logger.info("Saving ticket: {}", ticket);
        String query = "INSERT INTO Tickets (numberOfSeats, idClient, idMatch) VALUES (?, ?, ?)";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, ticket.getNumberOfSeats());
            statement.setInt(2, ticket.getClient().getId());
            statement.setInt(3, ticket.getMatch().getId());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    ticket.setId(generatedKeys.getInt(1));
                }
                return ticket;
            }
        } catch (SQLException e) {
            logger.error("Error saving ticket", e);
        }
        return null;
    }

    @Override
    public Ticket delete(Integer id) {
        logger.info("Deleting ticket with id: {}", id);
        Ticket ticket = findOne(id);
        if (ticket == null) return null;
        String query = "DELETE FROM Tickets WHERE id = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return ticket;
            }
        } catch (SQLException e) {
            logger.error("Error deleting ticket", e);
        }
        return null;
    }

    @Override
    public Ticket update(Ticket ticket) {
        logger.info("Updating ticket: {}", ticket);
        String query = "UPDATE Tickets SET numberOfSeats = ?, idClient = ?, idMatch = ? WHERE id = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ticket.getNumberOfSeats());
            statement.setInt(2, ticket.getClient().getId());
            statement.setInt(3, ticket.getMatch().getId());
            statement.setInt(4, ticket.getId());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return ticket;
            }
        } catch (SQLException e) {
            logger.error("Error updating ticket", e);
        }
        return null;
    }

    @Override
    public List<Ticket> findAllByClientId(Integer clientId) {
        logger.info("Finding tickets for client with id: {}", clientId);
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Tickets WHERE idClient = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, clientId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                tickets.add(buildTicketFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding tickets by client id", e);
        }
        return tickets;
    }

    private Ticket buildTicketFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int numberOfSeats = rs.getInt("numberOfSeats");
        int clientId = rs.getInt("idClient");
        int matchId = rs.getInt("idMatch");

        Client client = clientRepo.findOne(clientId);
        Match match = matchRepo.findOne(matchId);

        Ticket ticket = new Ticket(client, match, numberOfSeats);
        ticket.setId(id);
        return ticket;
    }
}
