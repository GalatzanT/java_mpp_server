package tudor.twoclient;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.BasketException;
import services.IServices;
import tudor.twoclient.HomeController;

public class LoginController {

    private IServices server;
    private HomeController homeCtrl;
    private Worker crtWorker;

    private static Logger logger = LogManager.getLogger(LoginController.class);
    @FXML
    TextField user;
    @FXML
    PasswordField password;

    @FXML
    private Button loginButton;

    Parent mainChatParent;

    public void setServer(IServices s){
        server=s;
    }


    public void setParent(Parent p){
        mainChatParent=p;
    }

    public void pressLogin(ActionEvent actionEvent) {
        //Parent root;
        logger.info("Login button pressed");
        String nume = user.getText();
        String passwd = password.getText();
        crtWorker = new Worker(nume, passwd);


        try{
            server.login(crtWorker, homeCtrl);
            // Util.writeLog("User succesfully logged in "+crtUser.getId());
            Stage stage=new Stage();
            stage.setTitle("Chat Window for " +crtWorker.getUsername());
            stage.setScene(new Scene(mainChatParent));
            homeCtrl.updateMatches();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    homeCtrl.logout();
                    logger.debug("Closing application");
                    System.exit(0);
                }
            });

            stage.show();
            homeCtrl.setWorker(crtWorker);
            //chatCtrl.setLoggedFriends();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }   catch (BasketException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP chat");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }


    }





    public void pressCancel(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setWorker(Worker worker) {
        this.crtWorker = worker;
    }

    public void setHomeController(HomeController homeController) {
        this.homeCtrl = homeController;
    }


}
