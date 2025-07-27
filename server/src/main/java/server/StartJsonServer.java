package server;

import networking.utils.AbstractServer;
import networking.utils.ChatJsonConcurrentServer;
import networking.utils.ServerException;


import model.Ticket;
import networking.utils.AbstractServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.ClientRepo;
import persistence.DB.dbClientRepo;
import persistence.DB.dbMatchRepo;
import persistence.DB.dbTicketRepo;
import persistence.DB.dbWorkerRepo;
import persistence.MatchRepo;
import persistence.TicketRepo;
import persistence.WorkerRepo;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartJsonServer {
    private static int defaultPort=55555;
    private static Logger logger = LogManager.getLogger(StartJsonServer.class);
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartJsonServer.class.getResourceAsStream("/server.properties"));
           logger.info("Server properties set. {} ", serverProps);
            //serverProps.list(System.out);
        } catch (IOException e) {
            logger.error("Cannot find chatserver.properties "+e);
            logger.debug("Looking for file in "+(new File(".")).getAbsolutePath());
            return;
        }
        //initializam repsozitorii
//        WorkerRepo workerRepo=new dbWorkerRepo(serverProps);
//        ClientRepo clientRepo=new dbClientRepo(serverProps);
//        MatchRepo matchRepo=new dbMatchRepo(serverProps);
//        TicketRepo ticketRepo=new dbTicketRepo(serverProps,clientRepo,matchRepo);


        //Hibernate repositories
        WorkerRepo workerRepo = new persistence.Hibernate.WorkerRepoDb();
        ClientRepo clientRepo = new persistence.Hibernate.ClientRepoDb();
        MatchRepo matchRepo = new persistence.Hibernate.MatchRepoDb();
        TicketRepo ticketRepo = new persistence.Hibernate.TicketRepoDb();

        ServiceImpl serviceImpl=new ServiceImpl(clientRepo,matchRepo,ticketRepo,workerRepo);

        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            logger.error("Wrong  Port Number"+nef.getMessage());
            logger.debug("Using default port "+defaultPort);
        }
        logger.debug("Starting server on port: "+chatServerPort);

        AbstractServer server = new ChatJsonConcurrentServer(chatServerPort, serviceImpl);
        try {
            server.start();
        } catch (ServerException e) {
            logger.error("Error starting the server" + e.getMessage());
        }
    }
}
