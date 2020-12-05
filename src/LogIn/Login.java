package LogIn;

import utils.DBConnection;
import utils.DBQuery;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Login {

    private String username;
    private String password;
    private static String currentUser;
    private static int currentUserId;

    // Create constructor
    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Set getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Set setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String validUser(String userName, String password) throws SQLException {

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT userName, password FROM user";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            String userNameDB = rs.getString("userName");
            String passwordDB = rs.getString("password");

            if (userName.equals(userNameDB) && password.equals(passwordDB))
                return "Valid";

        }
        return "Invalid";
    }

    // Set getters
    public static String getCurrentUser(){
        return currentUser;
    }

    public static int getCurrentUserId(){
        return currentUserId;
    }

    // Set setters
    public static void setCurrentUserName(String userName){
        currentUser = userName;
    }

    public static void setCurrentUserId(String userName) throws SQLException {

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT * FROM user WHERE userName = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, userName);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            currentUserId = rs.getInt("userId");
        }
    }

}
