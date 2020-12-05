package Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import java.util.TimeZone;

public class CalendarTable {

    private int appointmentId;
    private int customerId;
    private String title;
    private String description;
    private String location;
    private String type;
    private String date;
    private String startTime;
    private String endTime;

    private static ObservableList<CalendarTable> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<CalendarTable> currentWeekAppointments = FXCollections.observableArrayList();
    private static ObservableList<CalendarTable> currentMonthAppointments = FXCollections.observableArrayList();
    private static ObservableList<CalendarTable> selectedDayAppointments = FXCollections.observableArrayList();

    // Constructor
    public CalendarTable(int appointmentId, int customerId, String title, String description, String location, String type, String date, String startTime, String endTime) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Set getters
    public int getAppointmentId() {
        return appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    // Set setters
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public static ObservableList<CalendarTable> getAllAppointments() throws SQLException {

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT * FROM appointment";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();

        ResultSet rs = ps.getResultSet();

        while(rs.next()){
                    int appointmentId = rs.getInt("appointmentId");
                    int customerId = rs.getInt("customerId");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String location = rs.getString("location");
                    String type = rs.getString("type");
                    Timestamp start = rs.getTimestamp("start");
                    Timestamp end = rs.getTimestamp("end");

                    //Convert Start Date and Time
                    LocalDateTime startDT = start.toLocalDateTime();
                    ZonedDateTime startTimeZDT = startDT.atZone(ZoneId.of("UTC"));
                    Instant startDTUTC = startTimeZDT.toInstant();
                    ZonedDateTime startToLocal = startDTUTC.atZone(ZoneId.of(TimeZone.getDefault().getID()));

                    //Convert Start Date and Time
                    LocalDateTime endDT = end.toLocalDateTime();
                    ZonedDateTime endTimeZDT = endDT.atZone(ZoneId.of("UTC"));
                    Instant endDTUTC = endTimeZDT.toInstant();
                    ZonedDateTime endToLocal = endDTUTC.atZone(ZoneId.of(TimeZone.getDefault().getID()));

                    //Format for columns
                    String dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy").format(startToLocal);
                    String startTimeFormat = DateTimeFormatter.ofPattern("HH:mm").format(startToLocal);
                    String endTimeFormat = DateTimeFormatter.ofPattern("HH:mm").format(endToLocal);

            allAppointments.add(new CalendarTable(appointmentId, customerId, title, description, location, type,
                    dateFormat, startTimeFormat, endTimeFormat));

        }
        return allAppointments;
    }

    public static ObservableList<CalendarTable> getCurrentWeekAppointments() throws SQLException {

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT * FROM appointment WHERE WEEKOFYEAR(start) = WEEKOFYEAR(NOW())";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();

        ResultSet rs = ps.getResultSet();

        while(rs.next()){
            int appointmentId = rs.getInt("appointmentId");
            int customerId = rs.getInt("customerId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String type = rs.getString("type");
            Timestamp start = rs.getTimestamp("start");
            Timestamp end = rs.getTimestamp("end");

            //Convert Start Date and Time
            LocalDateTime startDT = start.toLocalDateTime();
            ZonedDateTime startTimeZDT = startDT.atZone(ZoneId.of("UTC"));
            Instant startDTUTC = startTimeZDT.toInstant();
            ZonedDateTime startToLocal = startDTUTC.atZone(ZoneId.of(TimeZone.getDefault().getID()));

            //Convert Start Date and Time
            LocalDateTime endDT = end.toLocalDateTime();
            ZonedDateTime endTimeZDT = endDT.atZone(ZoneId.of("UTC"));
            Instant endDTUTC = endTimeZDT.toInstant();
            ZonedDateTime endToLocal = endDTUTC.atZone(ZoneId.of(TimeZone.getDefault().getID()));

            //Format for columns
            String dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy").format(startToLocal);
            String startTimeFormat = DateTimeFormatter.ofPattern("HH:mm").format(startToLocal);
            String endTimeFormat = DateTimeFormatter.ofPattern("HH:mm").format(endToLocal);

            currentWeekAppointments.add(new CalendarTable(appointmentId, customerId, title, description, location, type,
                    dateFormat, startTimeFormat, endTimeFormat));
        }
        return currentWeekAppointments;
    }

    public static ObservableList<CalendarTable> getCurrentMonthAppointments() throws SQLException {

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT * FROM appointment WHERE MONTH(start) = MONTH(NOW())";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();

        ResultSet rs = ps.getResultSet();

        while(rs.next()){
            int appointmentId = rs.getInt("appointmentId");
            int customerId = rs.getInt("customerId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String type = rs.getString("type");
            Timestamp start = rs.getTimestamp("start");
            Timestamp end = rs.getTimestamp("end");

            //Convert Start Date and Time
            LocalDateTime startDT = start.toLocalDateTime();
            ZonedDateTime startTimeZDT = startDT.atZone(ZoneId.of("UTC"));
            Instant startDTUTC = startTimeZDT.toInstant();
            ZonedDateTime startToLocal = startDTUTC.atZone(ZoneId.of(TimeZone.getDefault().getID()));

            //Convert Start Date and Time
            LocalDateTime endDT = end.toLocalDateTime();
            ZonedDateTime endTimeZDT = endDT.atZone(ZoneId.of("UTC"));
            Instant endDTUTC = endTimeZDT.toInstant();
            ZonedDateTime endToLocal = endDTUTC.atZone(ZoneId.of(TimeZone.getDefault().getID()));

            //Format for columns
            String dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy").format(startToLocal);
            String startTimeFormat = DateTimeFormatter.ofPattern("HH:mm").format(startToLocal);
            String endTimeFormat = DateTimeFormatter.ofPattern("HH:mm").format(endToLocal);

            currentMonthAppointments.add(new CalendarTable(appointmentId, customerId, title, description, location, type,
                    dateFormat, startTimeFormat, endTimeFormat));
        }
        return currentMonthAppointments;
    }

    public static void setSelectedDayAppointments(LocalDate selectedDate) throws SQLException {

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT * FROM appointment WHERE DATE(start) = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setDate(1, java.sql.Date.valueOf(selectedDate));
        ps.execute();

        ResultSet rs = ps.getResultSet();

        while(rs.next()){
            int appointmentId = rs.getInt("appointmentId");
            int customerId = rs.getInt("customerId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String type = rs.getString("type");
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

            //Lambda function to format time for Start and End Columns
            TimeColumnFormatter timeColumnFormatter = zonedDateTime -> {
                String timeFormatted =  DateTimeFormatter.ofPattern("HH:mm").format(zonedDateTime);
                return timeFormatted;
            };

            //Format for columns
            String dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy").format(startToLocal);
            String startTimeFormat = timeColumnFormatter.timeColumnFormatter(startToLocal);
            String endTimeFormat = timeColumnFormatter.timeColumnFormatter(endToLocal);

            selectedDayAppointments.add(new CalendarTable(appointmentId, customerId, title, description, location, type,
                    dateFormat, startTimeFormat, endTimeFormat));
        }
    }

    public static ObservableList<CalendarTable> getSelectedDayAppointments(){
        return selectedDayAppointments;
    }

    public static boolean appointmentTimeAlert(int currentUserId) throws SQLException {

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT * FROM appointment WHERE userId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, currentUserId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            Timestamp start = rs.getTimestamp("start");

            //Convert Start Date and Time
            LocalDateTime startDT = start.toLocalDateTime();
            ZonedDateTime startTimeZDT = startDT.atZone(ZoneId.of("UTC"));
            Instant startDTUTC = startTimeZDT.toInstant();
            ZonedDateTime startToLocal = startDTUTC.atZone(ZoneId.of(TimeZone.getDefault().getID()));

            LocalDateTime appointmentTimeConverted = startToLocal.toLocalDateTime();
            LocalDateTime currentTime = LocalDateTime.now();

            long timeDifference = ChronoUnit.MINUTES.between(appointmentTimeConverted, currentTime);

            if(timeDifference > 0 && timeDifference <=15){
                return true;
            }
        }
        return false;
    }

}
