package persistence.DB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.Worker;
import persistence.WorkerRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class dbWorkerRepo implements WorkerRepo{

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public dbWorkerRepo(Properties props) {
        logger.info("Initializing RepoDBWorker with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Worker findOne(Integer id) {
        logger.info("Finding worker with id: {}", id);
        String query = "SELECT * FROM Workers WHERE idWorker = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Worker(
                        resultSet.getString("username"),
                        resultSet.getString("password"));
            }
        } catch (SQLException e) {
            logger.error("Error finding worker", e);
        }
        return null;
    }

    @Override
    public List<Worker> findAll() {
        logger.info("Finding all workers");
        List<Worker> workers = new ArrayList<>();
        String query = "SELECT * FROM Workers";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                workers.add(new Worker(
                        resultSet.getString("username"),
                        resultSet.getString("password")));
            }
        } catch (SQLException e) {
            logger.error("Error finding workers", e);
        }
        return workers;
    }

    @Override
    public Worker save(Worker worker) {
        logger.info("Saving worker: {}", worker);
        String query = "INSERT INTO Workers (username, password) VALUES (?, ?)";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, worker.getUsername());
            statement.setString(2, worker.getPassword());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    worker.setId(generatedKeys.getInt(1));
                }
                return worker;
            }
        } catch (SQLException e) {
            logger.error("Error saving worker", e);
        }
        return null;
    }

    @Override
    public Worker delete(Integer id) {
        logger.info("Deleting worker with id: {}", id);
        Worker worker = findOne(id);
        if (worker == null) return null;
        String query = "DELETE FROM Workers WHERE idWorker = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return worker;
            }
        } catch (SQLException e) {
            logger.error("Error deleting worker", e);
        }
        return null;
    }

    @Override
    public Worker update(Worker worker) {
        logger.info("Updating worker: {}", worker);
        String query = "UPDATE Workers SET username = ?, password = ? WHERE idWorker = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, worker.getUsername());
            statement.setString(2, worker.getPassword());
            statement.setInt(3, worker.getId());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return worker;
            }
        } catch (SQLException e) {
            logger.error("Error updating worker", e);
        }
        return null;
    }

    @Override
    public Worker findBy(String username, String password) {
        logger.info("Finding worker with username: {} and password: {}", username, password);
        String query = "SELECT * FROM Workers WHERE username = ? AND password = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Worker w = new Worker(
                        resultSet.getString("username"),
                        resultSet.getString("password"));
                w.setId(resultSet.getInt("idWorker"));
                return w;
            }
        } catch (SQLException e) {
            logger.error("Error finding worker", e);
        }
        return null;
    }
}
