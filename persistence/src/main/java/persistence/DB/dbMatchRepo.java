package persistence.DB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.Match;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Repository;
import persistence.MatchRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@Repository
public class dbMatchRepo implements MatchRepo {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

   // @Autowired
    public dbMatchRepo( Properties props) {
        logger.info("Initializing dbMatchRepo with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Match findOne(Integer id) {
        logger.info("Finding match with id: {}", id);
        String query = "SELECT * FROM Matches WHERE id = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Match match = extractMatchFromResultSet(rs);
                match.setId(id);
                return match;
            }
        } catch (SQLException e) {
            logger.error("Error finding match", e);
        }
        return null;
    }
    @Override
    public Match findBy2(int id) {
        logger.info("Finding match with id: {}", id);
        String query = "SELECT * FROM Matches WHERE id = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Match match = extractMatchFromResultSet(rs);
                match.setId(id);
                return match;
            }
        } catch (SQLException e) {
            logger.error("Error finding match", e);
        }
        return null;
    }

    @Override
    public List<Match> findAll() {
        logger.info("Finding all matches");
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM Matches";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Match match = extractMatchFromResultSet(rs);
                match.setId(rs.getInt("id"));
                matches.add(match);
            }
        } catch (SQLException e) {
            logger.error("Error finding matches", e);
        }
        return matches;
    }

    @Override
    public Match save(Match match) {
        logger.info("Saving match: {}", match);
        String query = "INSERT INTO Matches (team1, team2, matchType, ticketPrice, availableSeats) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, match.getTeam1());
            statement.setString(2, match.getTeam2());
            statement.setString(3, match.getMatchType());
            statement.setInt(4, match.getTicketPrice());
            statement.setInt(5, match.getAvailableSeats());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    match.setId(generatedKeys.getInt(1));
                }
                return match;
            }
        } catch (SQLException e) {
            logger.error("Error saving match", e);
        }
        return null;
    }

    @Override
    public Match delete(Integer id) {
        logger.info("Deleting match with id: {}", id);
        Match match = findOne(id);
        if (match == null) return null;
        String query = "DELETE FROM Matches WHERE id = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return match;
            }
        } catch (SQLException e) {
            logger.error("Error deleting match", e);
        }
        return null;
    }

    @Override
    public Match update(Match match) {
        logger.info("Updating match: {}", match);
        String query = "UPDATE Matches SET team1 = ?, team2 = ?, matchType = ?, ticketPrice = ?, availableSeats = ? WHERE id = ?";
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, match.getTeam1());
            statement.setString(2, match.getTeam2());
            statement.setString(3, match.getMatchType());
            statement.setInt(4, match.getTicketPrice());
            statement.setInt(5, match.getAvailableSeats());
            statement.setInt(6, match.getId());
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return match;
            }
        } catch (SQLException e) {
            logger.error("Error updating match", e);
        }
        return null;
    }


    private Match extractMatchFromResultSet(ResultSet rs) throws SQLException {
        return new Match(
                rs.getString("team1"),
                rs.getString("team2"),
                rs.getString("matchType"),
                rs.getInt("ticketPrice"),
                rs.getInt("availableSeats")
        );
    }
}
