package Customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField addressTxt;

    @FXML
    private TextField phoneNumberTxt;

    @FXML
    private TextField address2Txt;

    @FXML
    private TextField cityTxt;

    @FXML
    private TextField zipCodeTxt;

    @FXML
    private TextField countryTxt;

    // Add customer button function
    @FXML
    void OnActionAddCustomer(ActionEvent event) throws IOException, SQLException {

        String customerName = nameTxt.getText();
        String address = addressTxt.getText();
        String address2 = address2Txt.getText();
        String city = cityTxt.getText();
        String postalCode = zipCodeTxt.getText();
        String country = countryTxt.getText();
        String phone = phoneNumberTxt.getText();

        if(customerName.isEmpty() || address.isEmpty() || city.isEmpty() || postalCode.isEmpty() ||
                country.isEmpty() || phone.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please complete form before saving.");
            alert.showAndWait();
        }else {

            Customer.createCustomer(customerName, address, address2, postalCode, phone, city, country);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../Customer/ManageCustomers.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    // Cancel button function
    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to continue? Unsaved changes will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {


            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../Customer/ManageCustomers.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
