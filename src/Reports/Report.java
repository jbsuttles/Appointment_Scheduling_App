package Reports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;
import utils.DBQuery;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Report {

    private static ObservableList<Integer> userList = FXCollections.observableArrayList();

    public static void logUserLogin(String userName) throws IOException {

        //Filename
        String filename = "userlog.txt";

        //Create FileWriter Object
        FileWriter outputFile = new FileWriter(filename, true);

        //Create and open file
        PrintWriter pw = new PrintWriter(outputFile);
        pw.append("User " + userName + " has logged in at " + LocalDateTime.now()+"\n");
        pw.close();
    }

    public static int distinctApptType () throws SQLException {

        //Est. connection
        Connection conn =DBConnection.getConnection();

        //Create query
        String selectStatment = "SELECT COUNT(DISTINCT type) FROM appointment WHERE MONTH(start) = MONTH(CURDATE()) ";
        DBQuery.setPreparedStatement(conn, selectStatment);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();
        int count = 0;
        while(rs.next()){
            count = rs.getInt("count(distinct type)");
        }
        return count;
    }

    public static ObservableList getAllUser() throws SQLException {
        //Est. Connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT * FROM user";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            userList.add(rs.getInt("userId"));
        }
        return userList;
    }

    public static int userAppts(int userId) throws SQLException {
        //Est. Connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT COUNT(userId) FROM appointment WHERE userId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, userId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        int userAppts= 0;
        while(rs.next()){
            userAppts = rs.getInt("COUNT(userId)");
        }
        return userAppts;
    }

    public static int customerCount() throws SQLException {
        //Est. Connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT COUNT(customerId) FROM customer";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();
        int customerCount = 0;
        while (rs.next()){
            customerCount = rs.getInt("COUNT(customerId)");
        }
        return customerCount;
    }
}
