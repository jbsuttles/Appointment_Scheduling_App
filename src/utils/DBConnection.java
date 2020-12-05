package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    //JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U07fFr";

    //JDBC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    //Drive Interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn;

    //Username and Password
    private static final String username = "U07fFr";
    private static final String password = "53689013549";

    public static Connection startConnection(){
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");
        }
        catch(ClassNotFoundException e){
            System.out.println("Connection Error: " + e.getMessage());
        }
        catch (SQLException e){
            System.out.println("Connection Error: " + e.getMessage());
        }
        return conn;
    }

    public static void closeConnection(){
        try{
            conn.close();
            System.out.println("Connection Closed");
        }catch (SQLException e){
            System.out.println("Connection Error: " + e.getMessage());
        }
    }

    public static Connection getConnection(){
        return conn;
    }

}
