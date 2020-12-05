package Appointment;

import LogIn.Login;
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
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class UpdateAppointmentController implements Initializable {

    Stage stage;
    Parent scene;


    @FXML
    private
    TextField titleTxt;

    @FXML
    private TextField locationTxt;

    @FXML
    private TextField contactTxt;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField urlTxt;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private TextField customerInfoTxt;

    @FXML
    private ComboBox<LocalTime> startTimeBox;

    @FXML
    private ComboBox<LocalTime> endTimeBox;

    @FXML
    private DatePicker dateSelected;

    @FXML
    private TextField apptIdTxt;

    @FXML
    void OnActionCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to continue? Unsaved changes will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../Calendar/Calendar.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    @FXML
    void OnActionUpdateAppt(ActionEvent event) throws IOException, SQLException, ParseException {

        int appointmentId = Integer.parseInt(apptIdTxt.getText());
        String customerInformation = customerInfoTxt.getText();
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

        if(title.isEmpty() || description.isEmpty() || location.isEmpty() || type.isEmpty() ||
                dateSelected.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter required information.");
            alert.showAndWait();
        }else if (start.isAfter(end) || start.equals(end)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Start time must be before End time.");
            alert.showAndWait();
        }else if (Appointment.checkCustomerAppointment(appointmentId, customerId, date, start, end)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Customer already has an appointment during this time.");
            alert.showAndWait();
        }else if(Appointment.checkUserAvailability(appointmentId, Login.getCurrentUserId(), date, start, end)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("User already has an appointment scheduled for this time.");
            alert.showAndWait();
        }else {

            Appointment.updateAppointment(appointmentId, title, description, location, contact, type, url, date,
                    start, end);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../Calendar/Calendar.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    public void setAppointmentData(int appointmentId) throws SQLException {

        int customerId = 0;
        String customerName = null;
        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Set Appointment ID
        apptIdTxt.setText(String.valueOf(appointmentId));

        //Set Appointment Data
        String selectStatement = "SELECT * FROM appointment WHERE appointmentId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, appointmentId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            customerId = rs.getInt("customerId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String type = rs.getString("type");
            String url = rs.getString("url");
            Date date = rs.getDate("start");

            Timestamp start = rs.getTimestamp("start");
            Timestamp end = rs.getTimestamp("end");

            //Convert Start Date and Time
            LocalDateTime startDT = start.toLocalDateTime();
            ZonedDateTime startTimeZDT = startDT.atZone(ZoneId.of("UTC"));
            Instant startDTUTC = startTimeZDT.toInstant();
            ZonedDateTime startToLocal = startDTUTC.atZone(ZoneId.of(TimeZone.getDefault().getID()));

            //Convert End Date and Time
            LocalDateTime endDT = end.toLocalDateTime();
            ZonedDateTime endTimeZDT = endDT.atZone(ZoneId.of("UTC"));
            Instant endDTUTC = endTimeZDT.toInstant();
            ZonedDateTime endToLocal = endDTUTC.atZone(ZoneId.of(TimeZone.getDefault().getID()));

            //Lambda function to format time for Start and End combo boxes
            TimeComoBoxFormatter timeFormatConverter = zonedDateTime -> {
                String timeFormatted =  DateTimeFormatter.ofPattern("HH:mm").format(zonedDateTime);
                LocalTime timeConverted = LocalTime.parse(timeFormatted);
                return timeConverted;
            };

            LocalTime startTimeLocal = timeFormatConverter.timeFormatConversation(startToLocal);
            LocalTime endTimeLocal = timeFormatConverter.timeFormatConversation(endToLocal);


            //Set Text Fields
            titleTxt.setText(title);
            descriptionTxt.setText(description);
            locationTxt.setText(location);
            contactTxt.setText(contact);
            typeComboBox.setValue(type);
            urlTxt.setText(url);
            dateSelected.setValue(date.toLocalDate());
            startTimeBox.setValue(startTimeLocal);
            endTimeBox.setValue(endTimeLocal);
        }

        //Set Customer field
        selectStatement = "SELECT * FROM customer WHERE customerId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setInt(1, customerId);
        ps.execute();

        rs = ps.getResultSet();
        while(rs.next()){
            customerName = rs.getString("customerName");
        }
        String customerIdConvert = String.valueOf(customerId);
        customerInfoTxt.setText(customerIdConvert + " - " + customerName);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Set Type Combo Box
        typeComboBox.setItems(Appointment.appointmentType);

        //Set Time Combo Box
        LocalTime start = LocalTime.of(8,0);
        LocalTime end = LocalTime.of(17,0);

        while (start.isBefore(end.plusSeconds(1))){
            startTimeBox.getItems().add(start);
            endTimeBox.getItems().add(start);
            start = start.plusMinutes(15);
        }
        startTimeBox.setVisibleRowCount(6);
        endTimeBox.setVisibleRowCount(6);
    }

}

