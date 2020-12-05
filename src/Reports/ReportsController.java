package Reports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private ComboBox<Integer> selectUser;

    @FXML
    private TextField userApptCount;

    @FXML
    private TextField distinctApptField;

    @FXML
    private TextField customerCountField;

    // Select User from drop down function
    @FXML
    void onActionSelectUser(ActionEvent event) throws SQLException {

        if(selectUser.getValue() != null) {

            int userId = selectUser.getValue();

            int userAppts = Report.userAppts(userId);

            userApptCount.setText(String.valueOf(userAppts));
        }

    }

    // Back button function
    @FXML
    void OnActionBack(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../Calendar/Calendar.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

        //Clear Combo Box
        selectUser.getItems().clear();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            distinctApptField.setText(String.valueOf(Report.distinctApptType()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            selectUser.setItems(Report.getAllUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            customerCountField.setText(String.valueOf(Report.customerCount()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
