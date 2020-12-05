package Calendar;

import Appointment.Appointment;
import Appointment.UpdateAppointmentController;
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
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    Stage stage;
    Parent scene;


    @FXML
    private TableView<CalendarTable> appointmentTableView;

    @FXML
    private TableColumn<CalendarTable, Integer> apptIdCol;

    @FXML
    private TableColumn<CalendarTable, Integer> customerIdCol;

    @FXML
    private TableColumn<CalendarTable, String> titleCol;

    @FXML
    private TableColumn<CalendarTable, String> descriptionCol;

    @FXML
    private TableColumn<CalendarTable, String> locationCol;

    @FXML
    private TableColumn<CalendarTable, String> typeCol;

    @FXML
    private TableColumn<CalendarTable, Date> dateCol;

    @FXML
    private TableColumn<CalendarTable, Time> startTimeCol;

    @FXML
    private TableColumn<CalendarTable, Time> endTimeCol;

    @FXML
    private RadioButton viewCurrentWeek;

    @FXML
    private ToggleGroup CalendarTG;

    @FXML
    private RadioButton viewCurrentMonth;

    @FXML
    private RadioButton viewAll;

    @FXML
    private DatePicker DatePicker;

    // Add appointment button function
    @FXML
    void OnActionAddAppt(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../Appointment/AddAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

        //Clear tableview
        appointmentTableView.getItems().clear();

    }

    // Manage Customers button function
    @FXML
    void OnActionManageCustomers(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../Customer/ManageCustomers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

        //Clear tableview
        appointmentTableView.getItems().clear();

    }

    // Display reports button function
    @FXML
    void OnActionDisplayReports(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../Reports/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

        //Clear tableview
        appointmentTableView.getItems().clear();

    }

    // Logout button function
    @FXML
    void OnActionLogout(ActionEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../LogIn/LogIn.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

        //Clear tableview
        appointmentTableView.getItems().clear();

    }

    // Select Date function
    @FXML
    void OnActionSelectDate(ActionEvent event) throws SQLException {

        if (DatePicker.getValue() != null) {
            LocalDate selectedDate = DatePicker.getValue();
            CalendarTable.setSelectedDayAppointments(selectedDate);

            if (CalendarTable.getSelectedDayAppointments().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No appointments for selected date.");
                alert.setTitle("No appointments");
                alert.showAndWait();

            } else {
                appointmentTableView.getItems().clear();
                appointmentTableView.setItems(CalendarTable.getSelectedDayAppointments());

            }
            DatePicker.setValue(null);
            DatePicker.getEditor().clear();
        }

    }

    // Update appointment button function
    @FXML
    void OnActionUpdateAppt(ActionEvent event) throws IOException, SQLException {

        ObservableList<CalendarTable> selected;
        selected = appointmentTableView.getSelectionModel().getSelectedItems();
        if(selected.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select an appointment to update.");
            alert.showAndWait();
        }else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Appointment/UpdateAppointment.fxml"));
            loader.load();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
            for (CalendarTable calendarTable : selected) {
                int appointmentId = calendarTable.getAppointmentId();
                UpdateAppointmentController UpdateAppointmentController = loader.getController();
                UpdateAppointmentController.setAppointmentData(appointmentId);
            }

            //Clear tableview
            appointmentTableView.getItems().clear();
        }
    }

    // View All appointments radio button function
    @FXML
    void OnActionViewAll(ActionEvent event) throws SQLException {


        if(viewAll.isSelected()){
            appointmentTableView.getItems().clear();
            appointmentTableView.setItems(CalendarTable.getAllAppointments());
        }
    }

    //View all appointments for current month radio button function
    @FXML
    void onActionCurrentMonth(ActionEvent event) throws SQLException {

        if(viewCurrentMonth.isSelected()){
            appointmentTableView.getItems().clear();
            appointmentTableView.setItems(CalendarTable.getCurrentMonthAppointments());

            if (appointmentTableView.getItems().isEmpty()) {
                if (CalendarTable.getCurrentMonthAppointments().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No appointments for current month.");
                    alert.setTitle("No appointments");
                    alert.showAndWait();
                }
            }
        }

    }

    // View all appointments for current week radio button function
    @FXML
    void onActionCurrentWeek(ActionEvent event) throws SQLException {

        if (viewCurrentWeek.isSelected()) {
            appointmentTableView.getItems().clear();
            appointmentTableView.setItems(CalendarTable.getCurrentWeekAppointments());

            if (appointmentTableView.getItems().isEmpty()) {
                if (CalendarTable.getCurrentWeekAppointments().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No appointments for current week.");
                    alert.setTitle("No appointments");
                    alert.showAndWait();
                }
            }

        }

    }

    // Delete appointment button function
    @FXML
    void onActionDeleteAppt(ActionEvent event) throws SQLException {

        ObservableList<CalendarTable> selected;
        selected = appointmentTableView.getSelectionModel().getSelectedItems();
        if (selected.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select an appointment to delete.");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                appointmentTableView.getItems().remove(selected);
                for (CalendarTable calendarTable : selected) {
                    int appointmentId = calendarTable.getAppointmentId();
                    Appointment.deleteAppointment(appointmentId);
                }
                appointmentTableView.getItems().clear();
                try {
                    appointmentTableView.setItems(CalendarTable.getAllAppointments());
                    viewAll.setSelected(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        try {
            appointmentTableView.setItems(CalendarTable.getAllAppointments());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
