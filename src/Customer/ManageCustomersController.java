package Customer;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageCustomersController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<CustomerTable> customerTableView;

    @FXML
    private TableColumn<CustomerTable, String> customerIdCol;

    @FXML
    private TableColumn<CustomerTable, String> customerNameCol;

    // Add customer button function
    @FXML
    void OnActionAddCustomer(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../Customer/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

        //Reset tableview
        customerTableView.getItems().clear();

    }

    // Back button function
    @FXML
    void OnActionBack(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../Calendar/Calendar.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

        //Reset tableview
        customerTableView.getItems().clear();

    }

    // Delete customer button function
    @FXML
    void OnActionDeleteCustomer(ActionEvent event) throws SQLException {

        ObservableList<CustomerTable> selected;
        selected = customerTableView.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select a customer to delete.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer? " +
                    "All associated appointments will be deleted as well.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                customerTableView.getItems().remove(selected);
                for (CustomerTable customerTable : selected) {
                    int customerId = customerTable.getCustomerId();
                    Customer.deleteCustomer(customerId);
                }

                //Reset tableview
                customerTableView.getItems().clear();
                try {
                    customerTableView.setItems(CustomerTable.getCustomerList());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Update customer button function
    @FXML
    void OnUpdateCustomer(ActionEvent event) throws IOException, SQLException {

        ObservableList<CustomerTable> selected;
        selected = customerTableView.getSelectionModel().getSelectedItems();
        if(selected.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select a customer to update.");
                alert.showAndWait();
        }else {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Customer/UpdateCustomer.fxml"));
            loader.load();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
                for (CustomerTable customerTable : selected) {
                    int customerId = customerTable.getCustomerId();
                    UpdateCustomerController UpdateCustomerController = loader.getController();
                    UpdateCustomerController.setCustomerData(customerId);
                }
            //Reset tableview
            customerTableView.getItems().clear();
            }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            customerTableView.setItems(CustomerTable.getCustomerList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

    }
}

