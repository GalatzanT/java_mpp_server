package tudor.twoclient;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import networking.jsonprotocol.ServiceRPCPRoxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.IServices;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartJsonClientFX extends Application {
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    private static Logger logger = LogManager.getLogger(StartJsonClientFX.class);

    public void start(Stage primaryStage) throws Exception {
        try {

            logger.debug("In start");
            Properties clientProps = new Properties();
            try {
                clientProps.load(StartJsonClientFX.class.getResourceAsStream("/client.properties"));
                logger.info("Client properties set {} ", clientProps);
                clientProps.list(System.out);
            } catch (IOException e) {
                logger.error("Cannot find chatclient.properties " + e);
                logger.debug("Looking for chatclient.properties in folder {}", (new File(".")).getAbsolutePath());
                return;
            }
            String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
            int serverPort = defaultChatPort;

            try {
                serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
            } catch (NumberFormatException ex) {
                logger.error("Wrong port number " + ex.getMessage());
                logger.debug("Using default port: " + defaultChatPort);
            }
            logger.info("Using server IP " + serverIP);
            logger.info("Using server port " + serverPort);

            IServices server = new ServiceRPCPRoxy(serverIP, serverPort);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/login-view.fxml"));
          //  C:\Users\tudor\IdeaProjects\MPPtry3\ONEclient\src\main\resources\views\login-view.fxml
            Parent root = loader.load();


        LoginController ctrl =
                loader.getController();
        ctrl.setServer(server);


            FXMLLoader cloader = new FXMLLoader(
                    getClass().getResource("/views/main-view.fxml"));
            Parent croot = cloader.load();


        HomeController homeCtrl = cloader.getController();
        homeCtrl.setServer(server);

        ctrl.setHomeController(homeCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("Basket Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        }catch (IOException e) {
            logger.error("Error loading FXML files: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }


    }
}
