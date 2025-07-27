package persistence.Hibernate;

import model.Worker;
import persistence.WorkerRepo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class WorkerRepoDb implements WorkerRepo {
    private static final Logger logger = LogManager.getLogger();
    private final SessionFactory sessionFactory;

    public WorkerRepoDb() {
        this.sessionFactory = new Configuration()
                .configure()                     // hibernate.cfg.xml
                .addAnnotatedClass(Worker.class)
                .buildSessionFactory();
    }

    public void close() {
        sessionFactory.close();
    }

    @Override
    public Worker findBy(String username, String password) {
        logger.info("Searching for Worker by username and password");
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Worker WHERE username = :username AND password = :password";
            return session.createQuery(hql, Worker.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error in findBy", e);
            return null;
        }
    }


    @Override
    public Worker findOne(Integer id) {
        logger.info("Finding worker by id: {}", id);
        try (Session session = sessionFactory.openSession()) {
            return session.find(Worker.class, id);
        } catch (Exception e) {
            logger.error("Error in findOne", e);
            return null;
        }
    }

    @Override
    public List<Worker> findAll() {
        logger.info("Finding all workers");
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Worker", Worker.class).list();
        }
    }

    @Override
    public Worker save(Worker worker) {
        logger.info("Saving worker: {}", worker);
        try {
            sessionFactory.inTransaction(session -> session.persist(worker));
            return worker;
        } catch (Exception e) {
            logger.error("Error saving worker", e);
            return null;
        }
    }

    @Override
    public Worker delete(Integer id) {
        logger.info("Deleting worker with id: {}", id);
        try {
            final Worker[] deletedWorker = {null};
            sessionFactory.inTransaction(session -> {
                Worker worker = session.find(Worker.class, id);
                if (worker != null) {
                    session.remove(worker);
                    deletedWorker[0] = worker;
                }
            });
            return deletedWorker[0];
        } catch (Exception e) {
            logger.error("Error deleting worker", e);
            return null;
        }
    }

    @Override
    public Worker update(Worker worker) {
        logger.info("Updating worker: {}", worker);
        try {
            sessionFactory.inTransaction(session -> session.merge(worker));
            return worker;
        } catch (Exception e) {
            logger.error("Error updating worker", e);
            return null;
        }
    }
}
