package LogIn;

import Calendar.CalendarTable;
import Reports.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Label ProgramName;

    @FXML
    private Label Username;

    @FXML
    private Label Password;

    @FXML
    private TextField usernameTxt;

    @FXML
    private PasswordField passwordTxt;


    @FXML
    private Button LoginButton;

    @FXML
    private Button CancelButton;

    @FXML
    void OnActionCancel(ActionEvent event) {
        DBConnection.closeConnection();
        System.exit(0);

    }

    @FXML
    void OnActionLogIn(ActionEvent event) throws IOException, SQLException {

        //Set english language
        Locale english = new Locale("en", "US");

        //Set spanish language
        Locale espanol = new Locale("es", "ES");

        //Resource Bundle
        ResourceBundle rb = ResourceBundle.getBundle("message", Locale.getDefault());


        String userName = usernameTxt.getText();
        String password = passwordTxt.getText();

        if(userName.isEmpty()|| password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            if(Locale.getDefault().getLanguage().equals("en")||(Locale.getDefault().getLanguage().equals("es"))){
                alert.setTitle(rb.getString("warning"));
                alert.setContentText(rb.getString("loginError"));
            }
            alert.showAndWait();
        }else {

            //Call LogIn Class
            String validateUser = Login.validUser(userName, password);

            if(validateUser.equals("Valid")){

                Login.setCurrentUserName(userName);
                Login.setCurrentUserId(userName);

                //Log user login
                Report.logUserLogin(Login.getCurrentUser());

                //Successful login
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("../Calendar/Calendar.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

                //Appointment 15 minute alert
                try {
                    if(CalendarTable.appointmentTimeAlert(Login.getCurrentUserId())){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("You have an appointment within the next 15 minutes.");
                        alert.showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            } else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                if(Locale.getDefault().getLanguage().equals("en")||(Locale.getDefault().getLanguage().equals("es"))){
                    alert.setTitle(rb.getString("warning"));
                    alert.setContentText(rb.getString("loginInvalid"));
                }
                alert.showAndWait();
                usernameTxt.setText("");
                passwordTxt.setText("");
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Resource Bundle
        ResourceBundle rb = ResourceBundle.getBundle("message", Locale.getDefault());

        //Set language of login form
        if(Locale.getDefault().getLanguage().equals("en")||(Locale.getDefault().getLanguage().equals("es"))){
            Username.setText(rb.getString("username"));
            Password.setText(rb.getString("password"));
            ProgramName.setText(rb.getString("ProgramName"));
            LoginButton.setText(rb.getString("Login"));
            CancelButton.setText(rb.getString("Cancel"));
        }
    }
}
