    package persistence.Hibernate;


    import model.Match;
    import persistence.MatchRepo;
    import org.hibernate.Session;
    import org.hibernate.SessionFactory;
    import org.hibernate.cfg.Configuration;
    import org.apache.logging.log4j.LogManager;
    import org.apache.logging.log4j.Logger;

    import java.util.List;

    public class MatchRepoDb implements MatchRepo {
        private static final Logger logger = LogManager.getLogger();
        private final SessionFactory sessionFactory;

        public MatchRepoDb() {
            this.sessionFactory = new Configuration()
                    .configure() // loads hibernate.cfg.xml
                    .addAnnotatedClass(Match.class)
                    .buildSessionFactory();
        }

        public void close() {
            sessionFactory.close();
        }

        @Override
        public Match findOne(Integer id) {
            logger.info("Finding match with id: {}", id);
            try (Session session = sessionFactory.openSession()) {
                return session.find(Match.class, id);
            } catch (Exception e) {
                logger.error("Error finding match", e);
                return null;
            }
        }

        @Override
        public List<Match> findAll() {
            logger.info("Finding all matches");
            try (Session session = sessionFactory.openSession()) {
                return session.createQuery("FROM Match", Match.class).list();
            } catch (Exception e) {
                logger.error("Error finding matches", e);
                return null;
            }
        }

        @Override
        public Match save(Match match) {
            logger.info("Saving match: {}", match);
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.persist(match);
                session.getTransaction().commit();
                return match;
            } catch (Exception e) {
                logger.error("Error saving match", e);
                return null;
            }
        }

        @Override
        public Match delete(Integer id) {
            logger.info("Deleting match with id: {}", id);
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                Match match = session.find(Match.class, id);
                if (match != null) {
                    session.remove(match);
                    session.getTransaction().commit();
                    return match;
                }
                session.getTransaction().rollback();
            } catch (Exception e) {
                logger.error("Error deleting match", e);
            }
            return null;
        }

        @Override
        public Match update(Match match) {
            logger.info("Updating match: {}", match);
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.merge(match);
                session.getTransaction().commit();
                return match;
            } catch (Exception e) {
                logger.error("Error updating match", e);
                return null;
            }
        }

        @Override
        public Match findBy2(int id) {
            return null;
        }
    }
