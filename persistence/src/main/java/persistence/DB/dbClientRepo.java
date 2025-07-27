package persistence.DB;

import model.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.Client;
import persistence.ClientRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class dbClientRepo implements ClientRepo {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public dbClientRepo(Properties props) {
        logger.info("Initializing dbClientRepo with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Client findOne(Integer id) {
        logger.info("Finding client with id: {}", id);
        String query = "SELECT * FROM Clients WHERE id = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Client client = new Client(
                        resultSet.getString("name"),
                        resultSet.getString("email"));
                client.setId(resultSet.getInt("id"));
                return client;
            }
        } catch (SQLException e) {
            logger.error("Error finding client", e);
        }
        return null;
    }

    @Override
    public List<Client> findAll() {
        logger.info("Finding all clients");
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM Clients";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getString("name"),
                        resultSet.getString("email"));
                client.setId(resultSet.getInt("id"));
                clients.add(client);
            }
        } catch (SQLException e) {
            logger.error("Error finding clients", e);
        }
        return clients;
    }

    @Override
    public Client save(Client client) {
        logger.info("Saving client: {}", client);
        String query = "INSERT INTO Clients (name, email) VALUES (?, ?)";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getAddres());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                }
                return client;
            }
        } catch (SQLException e) {
            logger.error("Error saving client", e);
        }
        return null;
    }

    @Override
    public Client delete(Integer id) {
        logger.info("Deleting client with id: {}", id);
        Client client = findOne(id);
        if (client == null) return null;
        String query = "DELETE FROM Clients WHERE id = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return client;
            }
        } catch (SQLException e) {
            logger.error("Error deleting client", e);
        }
        return null;
    }

    @Override
    public Client update(Client client) {
        logger.info("Updating client: {}", client);
        String query = "UPDATE Clients SET name = ?, email = ? WHERE id = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getAddres());
            statement.setInt(3, client.getId());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return client;
            }
        } catch (SQLException e) {
            logger.error("Error updating client", e);
        }
        return null;
    }

    @Override
    public Client findBy(String username, String address) {
        String query = "SELECT * FROM Clients WHERE name = ? AND email = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, address);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Client(
                        resultSet.getString("name"),
                        resultSet.getString("email"));
            }
        } catch (SQLException e) {
            logger.error("Error finding client by username and address", e);
        }
        return null;
    }
}
