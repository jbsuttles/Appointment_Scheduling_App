package Appointment;

import Customer.Customer;
import LogIn.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField titleTxt;

    @FXML
    private TextField locationTxt;

    @FXML
    private TextField contactTxt;


    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField urlTxt;

    @FXML
    private ComboBox<LocalTime> startTimeBox;

    @FXML
    private ComboBox<LocalTime> endTimeBox;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private DatePicker dateSelected;

    @FXML
    private ComboBox<String> customerBox;

    // Add appointment button function
    @FXML
    void OnActionAddAppt(ActionEvent event) throws IOException, SQLException, ParseException {

        if(customerBox.getValue() == null){
            System.out.println("no customer");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select a customer.");
            alert.showAndWait();
        }else {

            String customerInformation = customerBox.getValue();
            int customerId = NumberFormat.getInstance().parse(customerInformation).intValue();

            String title = titleTxt.getText();
            String description = descriptionTxt.getText();
            String location = locationTxt.getText();
            String contact = contactTxt.getText();
            String type = typeComboBox.getValue();
            String url = urlTxt.getText();
            LocalDate date = dateSelected.getValue();
            LocalTime start = startTimeBox.getValue();
            LocalTime end = endTimeBox.getValue();

            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() ||
                    dateSelected.getValue() == null) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("Please enter required information.");
                alert.showAndWait();
            } else if (start.isAfter(end) || start.equals(end)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("Start time must be before End time.");
                alert.showAndWait();
            } else if (Appointment.checkCustomerAppointment(customerId, date, start, end)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("Customer already has an appointment during this time.");
                alert.showAndWait();
            }else if(Appointment.checkUserAvailability(Login.getCurrentUserId(), date, start, end)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("User already has an appointment scheduled for this time.");
                alert.showAndWait();
            }else{

                Appointment.createAppointment(customerId, title, description, location, contact, type, url, date, start, end);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("../Calendar/Calendar.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

                //Clear Combo Box
                customerBox.getItems().clear();
            }
        }
    }

    // Cancel button function
    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to continue? Unsaved changes will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../Calendar/Calendar.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

            //Clear Combo Box
            customerBox.getItems().clear();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Set Customer Combo box
        customerBox.setPromptText("Choose a customer");

        try {
            customerBox.setItems(Customer.getCustomerList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Set Type Combo Box
        typeComboBox.setPromptText("Pick an Appointment Type");
        typeComboBox.setItems(Appointment.appointmentType);


        //Set Start/End Time Combo Boxes
        LocalTime start = LocalTime.of(8,0);
        LocalTime end = LocalTime.of(17,0);

        while (start.isBefore(end.plusSeconds(1))){
            startTimeBox.getItems().add(start);
            endTimeBox.getItems().add(start);
            start = start.plusMinutes(15);
        }
        startTimeBox.getSelectionModel().select(LocalTime.of(8,0));
        endTimeBox.getSelectionModel().select(LocalTime.of(8,0));
        startTimeBox.setVisibleRowCount(6);
        endTimeBox.setVisibleRowCount(6);
    }
}
