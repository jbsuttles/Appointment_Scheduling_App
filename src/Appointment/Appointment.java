package Appointment;

import LogIn.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Appointment {

    // Appointment Types
    public static ObservableList<String> appointmentType = FXCollections.observableArrayList("Scrum",
            "Marketing", "Presentation", "Lunch Meeting");


    // Constructor
    public static void createAppointment(int customerId, String title, String description, String location,
                                         String contact, String type, String url, LocalDate date, LocalTime start,
                                         LocalTime end) throws SQLException {

        //Convert StartDT to UTC
        LocalDateTime startDateTime = start.atDate(date);
        ZonedDateTime startDateTimeZDT = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime startDateTimeUTC = startDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Convert EndDT to UTC
        LocalDateTime endDateTime = end.atDate(date);
        ZonedDateTime endDateTimeZDT = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime endDateTimeUTC = endDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Format DateTime
       String startDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(startDateTimeUTC);
       String endDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(endDateTimeUTC);

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create Appointment
        String insertStatement = "INSERT INTO appointment(customerId, userId, title, description, location, contact, " +
                "type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?)";
        DBQuery.setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, customerId);
        ps.setInt(2, Login.getCurrentUserId());
        ps.setString(3, title);
        ps.setString(4, description);
        ps.setString(5, location);
        ps.setString(6, contact);
        ps.setString(7, type);
        ps.setString(8, url);
        ps.setString(9, startDateFormat);
        ps.setString(10, endDateFormat);
        ps.setString(11, Login.getCurrentUser());
        ps.setString(12, Login.getCurrentUser());
        ps.execute();

    }

    public static void updateAppointment(int appointmentId, String title, String description, String location,
                                         String contact, String type, String url, LocalDate date, LocalTime start,
                                         LocalTime end) throws SQLException {

        //Convert StartDT to UTC
        LocalDateTime startDateTime = start.atDate(date);
        ZonedDateTime startDateTimeZDT = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime startDateTimeUTC = startDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Convert EndDT to UTC
        LocalDateTime endDateTime = end.atDate(date);
        ZonedDateTime endDateTimeZDT = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime endDateTimeUTC = endDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Format DateTime
        String startDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(startDateTimeUTC);
        String endDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(endDateTimeUTC);

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create Appointment
        String insertStatement = "UPDATE appointment SET title = ?, description = ?, location = ?," +
                " contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdate = now(), lastUpdateBy = ? " +
                "WHERE appointmentId = ? ";
        DBQuery.setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, contact);
        ps.setString(5, type);
        ps.setString(6, url);
        ps.setString(7, startDateFormat);
        ps.setString(8, endDateFormat);
        ps.setString(9, Login.getCurrentUser());
        ps.setInt(10, appointmentId);

        ps.execute();

    }

    public static void deleteAppointment(int appointmentId) throws SQLException {

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Delete Query
        String deleteStatement = "DELETE FROM appointment WHERE appointmentId = ?";
        DBQuery.setPreparedStatement(conn, deleteStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, appointmentId);
        ps.execute();
    }

    public static boolean checkCustomerAppointment(int customerId, LocalDate date, LocalTime start, LocalTime end)
            throws SQLException {

        //Convert StartDT to UTC
        LocalDateTime startDateTime = start.atDate(date);
        ZonedDateTime startDateTimeZDT = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime startDateTimeUTC = startDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Convert EndDT to UTC
        LocalDateTime endDateTime = end.atDate(date);
        ZonedDateTime endDateTimeZDT = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime endDateTimeUTC = endDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create query
        String selectStatement = "SELECT * FROM appointment WHERE customerId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, customerId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            Timestamp startDB = rs.getTimestamp("start");
            Timestamp endDB = rs.getTimestamp("end");

            //Convert Database Start Date and Time
            LocalDateTime startDT = startDB.toLocalDateTime();
            ZonedDateTime startDBConverted = startDT.atZone(ZoneId.of("UTC"));

            //Convert Database End Date and Time
            LocalDateTime endDT = endDB.toLocalDateTime();
            ZonedDateTime endDBConverted = endDT.atZone(ZoneId.of("UTC"));

            if((startDateTimeUTC.isAfter(startDBConverted) && startDateTimeUTC.isBefore(endDBConverted)) ||
                    (endDateTimeUTC.isAfter(startDBConverted) && endDateTimeUTC.isBefore(endDBConverted)) ||
                    (startDateTimeUTC.equals(startDBConverted) && endDateTimeUTC.equals(endDBConverted))) {
                return true;
            }else if((startDBConverted.isAfter(startDateTimeUTC) && startDBConverted.isBefore(endDateTimeUTC)) ||
                    (endDBConverted.isAfter(startDateTimeUTC) && endDBConverted.isBefore(endDateTimeUTC))){
                return true;
            }
        }
        return false;
    }

    public static boolean checkCustomerAppointment(int appointmentId, int customerId, LocalDate date, LocalTime start, LocalTime end)
            throws SQLException {

        //Convert StartDT to UTC
        LocalDateTime startDateTime = start.atDate(date);
        ZonedDateTime startDateTimeZDT = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime startDateTimeUTC = startDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Convert EndDT to UTC
        LocalDateTime endDateTime = end.atDate(date);
        ZonedDateTime endDateTimeZDT = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime endDateTimeUTC = endDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create query
        String selectStatement = "SELECT * FROM appointment WHERE customerId = ? and appointmentId != ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, customerId);
        ps.setInt(2, appointmentId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            Timestamp startDB = rs.getTimestamp("start");
            Timestamp endDB = rs.getTimestamp("end");

            //Convert Database Start Date and Time
            LocalDateTime startDT = startDB.toLocalDateTime();
            ZonedDateTime startDBConverted = startDT.atZone(ZoneId.of("UTC"));

            //Convert Database End Date and Time
            LocalDateTime endDT = endDB.toLocalDateTime();
            ZonedDateTime endDBConverted = endDT.atZone(ZoneId.of("UTC"));

            if((startDateTimeUTC.isAfter(startDBConverted) && startDateTimeUTC.isBefore(endDBConverted)) ||
                    (endDateTimeUTC.isAfter(startDBConverted) && endDateTimeUTC.isBefore(endDBConverted)) ||
                    (startDateTimeUTC.equals(startDBConverted) && endDateTimeUTC.equals(endDBConverted))) {
                return true;
            }else if((startDBConverted.isAfter(startDateTimeUTC) && startDBConverted.isBefore(endDateTimeUTC)) ||
                    (endDBConverted.isAfter(startDateTimeUTC) && endDBConverted.isBefore(endDateTimeUTC))){
                return true;
            }
        }
        return false;
    }

    public static boolean checkUserAvailability(int userId, LocalDate date, LocalTime start, LocalTime end) throws SQLException {

        //Convert StartDT to UTC
        LocalDateTime startDateTime = start.atDate(date);
        ZonedDateTime startDateTimeZDT = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime startDateTimeUTC = startDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Convert EndDT to UTC
        LocalDateTime endDateTime = end.atDate(date);
        ZonedDateTime endDateTimeZDT = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime endDateTimeUTC = endDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create query
        String selectStatement = "SELECT * FROM appointment WHERE userId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, userId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            Timestamp startDB = rs.getTimestamp("start");
            Timestamp endDB = rs.getTimestamp("end");

            //Convert Database Start Date and Time
            LocalDateTime startDT = startDB.toLocalDateTime();
            ZonedDateTime startDBConverted = startDT.atZone(ZoneId.of("UTC"));

            //Convert Database End Date and Time
            LocalDateTime endDT = endDB.toLocalDateTime();
            ZonedDateTime endDBConverted = endDT.atZone(ZoneId.of("UTC"));

            if((startDateTimeUTC.isAfter(startDBConverted) && startDateTimeUTC.isBefore(endDBConverted)) ||
                    (endDateTimeUTC.isAfter(startDBConverted) && endDateTimeUTC.isBefore(endDBConverted)) ||
                    (startDateTimeUTC.equals(startDBConverted) && endDateTimeUTC.equals(endDBConverted))) {
                return true;
            }else if((startDBConverted.isAfter(startDateTimeUTC) && startDBConverted.isBefore(endDateTimeUTC)) ||
                    (endDBConverted.isAfter(startDateTimeUTC) && endDBConverted.isBefore(endDateTimeUTC))){
                return true;
            }
        }
        return false;
    }

    public static boolean checkUserAvailability(int appointmentId, int userId, LocalDate date, LocalTime start, LocalTime end) throws SQLException {

        //Convert StartDT to UTC
        LocalDateTime startDateTime = start.atDate(date);
        ZonedDateTime startDateTimeZDT = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime startDateTimeUTC = startDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Convert EndDT to UTC
        LocalDateTime endDateTime = end.atDate(date);
        ZonedDateTime endDateTimeZDT = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime endDateTimeUTC = endDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create query
        String selectStatement = "SELECT * FROM appointment WHERE userId = ? and appointmentId != ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, userId);
        ps.setInt(2, appointmentId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            Timestamp startDB = rs.getTimestamp("start");
            Timestamp endDB = rs.getTimestamp("end");

            //Convert Database Start Date and Time
            LocalDateTime startDT = startDB.toLocalDateTime();
            ZonedDateTime startDBConverted = startDT.atZone(ZoneId.of("UTC"));

            //Convert Database End Date and Time
            LocalDateTime endDT = endDB.toLocalDateTime();
            ZonedDateTime endDBConverted = endDT.atZone(ZoneId.of("UTC"));

            if((startDateTimeUTC.isAfter(startDBConverted) && startDateTimeUTC.isBefore(endDBConverted)) ||
                    (endDateTimeUTC.isAfter(startDBConverted) && endDateTimeUTC.isBefore(endDBConverted)) ||
                    (startDateTimeUTC.equals(startDBConverted) && endDateTimeUTC.equals(endDBConverted))) {
                return true;
            }else if((startDBConverted.isAfter(startDateTimeUTC) && startDBConverted.isBefore(endDateTimeUTC)) ||
                    (endDBConverted.isAfter(startDateTimeUTC) && endDBConverted.isBefore(endDateTimeUTC))){
                return true;
            }
        }
        return false;
    }

}


