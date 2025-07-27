package persistence.Hibernate;


import model.Client;
import persistence.ClientRepo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ClientRepoDb implements ClientRepo {
    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory;

    public ClientRepoDb() {
        this.sessionFactory = new Configuration()
                .configure()                    // loads hibernate.cfg.xml
                .addAnnotatedClass(Client.class)
                .buildSessionFactory();
    }

    public void close() {
        sessionFactory.close();
    }

    @Override
    public Client findBy(String name, String email) {
        logger.info("Searching for Client by name and email");
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Client WHERE name = :name AND email = :email";
            return session.createQuery(hql, Client.class)
                    .setParameter("name", name)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error in findBy", e);
            return null;
        }
    }

    @Override
    public Client findOne(Integer id) {
        logger.info("Finding client by id: {}", id);
        try (Session session = sessionFactory.openSession()) {
            return session.find(Client.class, id);
        } catch (Exception e) {
            logger.error("Error in findOne", e);
            return null;
        }
    }

    @Override
    public List<Client> findAll() {
        logger.info("Finding all clients");
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Client", Client.class).list();
        } catch (Exception e) {
            logger.error("Error in findAll", e);
            return null;
        }
    }

    @Override
    public Client save(Client client) {
        logger.info("Saving client: {}", client);
        try {
            sessionFactory.inTransaction(session -> session.persist(client));
            return client;
        } catch (Exception e) {
            logger.error("Error saving client", e);
            return null;
        }
    }

    @Override
    public Client delete(Integer id) {
        logger.info("Deleting client with id: {}", id);
        try {
            final Client[] deletedClient = {null};
            sessionFactory.inTransaction(session -> {
                Client client = session.find(Client.class, id);
                if (client != null) {
                    session.remove(client);
                    deletedClient[0] = client;
                }
            });
            return deletedClient[0];
        } catch (Exception e) {
            logger.error("Error deleting client", e);
            return null;
        }
    }

    @Override
    public Client update(Client client) {
        logger.info("Updating client: {}", client);
        try {
            sessionFactory.inTransaction(session -> session.merge(client));
            return client;
        } catch (Exception e) {
            logger.error("Error updating client", e);
            return null;
        }
    }
}
