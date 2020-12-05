package Customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable {

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

    @FXML
    private TextField customerIdTxt;

    // Cancel button function
    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to continue? Unsaved changes will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../Customer/ManageCustomers.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    // Update button function
    @FXML
    void OnActionUpdateCustomer(ActionEvent event) throws IOException, SQLException {

        int customerId = Integer.parseInt(customerIdTxt.getText());
        String customerName = nameTxt.getText();
        String address = addressTxt.getText();
        String address2 = address2Txt.getText();
        String city = cityTxt.getText();
        String postalCode = zipCodeTxt.getText();
        String country = countryTxt.getText();
        String phone = phoneNumberTxt.getText();

        if(customerName.isEmpty() || address.isEmpty() || city.isEmpty() || postalCode.isEmpty() ||
                country.isEmpty() || phone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please complete form before saving.");
            alert.showAndWait();
        }else {

            Customer.updateCustomer(customerId, customerName, address, address2, city, postalCode, country, phone);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../Customer/ManageCustomers.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    public void setCustomerData(int customerId) throws SQLException {
        //This method sets the customer data to UpdateCustomer scene

        //Est. connection
        Connection conn = DBConnection.getConnection();
        
        //Table reference IDs
        int addressId = 0;
        int cityId = 0;
        int countryId = 0;

        //Set Customer ID
        customerIdTxt.setText(String.valueOf(customerId));

        //Set Customer name
        String selectStatement = "SELECT * FROM customer WHERE customerId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, customerId);
        ps.execute();

        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            String customerName = rs.getString("customerName");
            addressId = rs.getInt("addressId");

            //Set info to fields
            nameTxt.setText(customerName);
        }

        //Set Customer address
        selectStatement = "SELECT * FROM address WHERE addressId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setInt(1, addressId);
        ps.execute();

        rs = ps.getResultSet();

        while(rs.next()){
            String address = rs.getString("address");
            String address2 = rs.getString("address2");
            cityId = rs.getInt("cityId");
            String postalCode = rs.getString("postalCode");
            String phone = rs.getString("phone");

            //Set info to fields
            addressTxt.setText(address);
            address2Txt.setText(address2);
            zipCodeTxt.setText(postalCode);
            phoneNumberTxt.setText(phone);
        }

        //Set Customer city
        selectStatement = "SELECT * FROM city WHERE cityId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setInt(1, cityId);
        ps.execute();

        rs = ps.getResultSet();

        while(rs.next()){
            String city = rs.getString("city");
            countryId = rs.getInt("countryId");

            //Set info to fields
            cityTxt.setText(city);
        }

        //Set Customer country
        selectStatement = "SELECT * FROM country WHERE countryId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setInt(1, countryId);
        ps.execute();

        rs = ps.getResultSet();

        while(rs.next()){
            String country = rs.getString("country");

            //Set info to fields
            countryTxt.setText(country);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
