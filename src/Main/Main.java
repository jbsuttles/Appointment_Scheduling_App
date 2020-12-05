package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../LogIn/LogIn.fxml"));
        primaryStage.setTitle("Appointment Scheduling App");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {

        // Start connection to database
        DBConnection.startConnection();

        launch(args);

        // Close connection to database
        DBConnection.closeConnection();
    }
}
