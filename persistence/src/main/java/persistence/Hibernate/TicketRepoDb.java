package persistence.Hibernate;

import model.Client;
import model.Match;
import model.Ticket;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.TicketRepo;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class TicketRepoDb implements TicketRepo {

    private final SessionFactory sessionFactory;

    public TicketRepoDb() {
        this.sessionFactory = new Configuration()
                .configure() // loads hibernate.cfg.xml
                .addAnnotatedClass(Ticket.class)
                .addAnnotatedClass(Client.class)   // if needed because of relations
                .addAnnotatedClass(Match.class)    // if needed because of relations
                .buildSessionFactory();
    }

    @Override
    public Ticket findOne(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Ticket.class, id);
        }
    }

    @Override
    public List<Ticket> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Ticket", Ticket.class).list();
        }
    }

    @Override
    public Ticket save(Ticket ticket) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(ticket);
            tx.commit();
            return ticket;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;  // propagate exception or handle as needed
        }
    }

    @Override
    public Ticket delete(Integer id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Ticket ticket = session.get(Ticket.class, id);
            if (ticket != null) {
                session.delete(ticket);
                tx.commit();
                return ticket;
            }
            return null;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Ticket update(Ticket ticket) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(ticket);
            tx.commit();
            return ticket;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<Ticket> findAllByClientId(Integer clientId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Ticket t WHERE t.client.id = :clientId";
            return session.createQuery(hql, Ticket.class)
                    .setParameter("clientId", clientId)
                    .list();
        }
    }
}
