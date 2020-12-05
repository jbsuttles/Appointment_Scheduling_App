package Customer;

import LogIn.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {

    private static ObservableList<String> customerList = FXCollections.observableArrayList();

    private int customerId;
    private String customerName;
    private String address;
    private String address1;
    private String postalCode;
    private String phone;
    private String city;
    private String country;


    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Constructor
    public Customer(int customerId, String customerName, String address, String address1, String postalCode, String phone, String city, String country) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.address1 = address1;
        this.postalCode = postalCode;
        this.phone = phone;
        this.city = city;
        this.country = country;


    }

    public static void createCustomer(String customerName, String address, String address2, String postalCode, String phone,
                                      String city, String country)throws SQLException {

        int countryId = 0;
        int cityId = 0;
        int addressId = 0;

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Create Customer Country
        String insertStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                " VALUES(?, now(), ?, now(), ?)";
        DBQuery.setPreparedStatement(conn, insertStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, country);
        ps.setString(2, Login.getCurrentUser());
        ps.setString(3, Login.getCurrentUser());
        ps.execute();

        String getCountryId = "SELECT LAST_INSERT_ID() FROM country";
        DBQuery.setPreparedStatement(conn, getCountryId);
        ps = DBQuery.getPreparedStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        if(rs.next()){
            countryId = rs.getInt(1);
        }

        //Create Customer City
        insertStatement = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                " VALUES(?, ?, now(), ?, now(), ?)";
        DBQuery.setPreparedStatement(conn, insertStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setString(1, city);
        ps.setInt(2, countryId);
        ps.setString(3, Login.getCurrentUser());
        ps.setString(4, Login.getCurrentUser());
        ps.execute();

        String getCityId = "SELECT LAST_INSERT_ID() FROM city";
        DBQuery.setPreparedStatement(conn, getCityId);
        ps = DBQuery.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();
        if(rs.next()){
            cityId = rs.getInt(1);
        }

        //Create Customer Address
        insertStatement = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                " VALUES(?, ?, ?, ?, ?, now(), ?, now(), ?)";
        DBQuery.setPreparedStatement(conn, insertStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setString(1, address);
        ps.setString(2, address2);
        ps.setInt(3, cityId);
        ps.setString(4, postalCode);
        ps.setString(5, phone);
        ps.setString(6, Login.getCurrentUser());
        ps.setString(7, Login.getCurrentUser());
        ps.execute();

        String getAddressId = "SELECT LAST_INSERT_ID() FROM address";
        DBQuery.setPreparedStatement(conn, getAddressId);
        ps = DBQuery.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();
        if(rs.next()){
            addressId = rs.getInt(1);
        }

        //Create Customer
        insertStatement = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                "VALUES(?, ?, 1, now(), ?, now(), ?)";
        DBQuery.setPreparedStatement(conn, insertStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setString(1, customerName);
        ps.setInt(2, addressId);
        ps.setString(3, Login.getCurrentUser());
        ps.setString(4, Login.getCurrentUser());
        ps.execute();
    }

    public static void updateCustomer(int customerId, String customerName, String address, String address2, String postalCode, String phone,
                                      String city, String country) throws SQLException {

        int addressId = 0;
        int cityId = 0;
        int countryId = 0;

        //Est. Connection
        Connection conn = DBConnection.getConnection();

        //Update Customer Name
        String updateStatement = "UPDATE customer SET customerName = ?, lastUpdate = now(), lastUpdateBy = ? WHERE customerId = ?";
        DBQuery.setPreparedStatement(conn, updateStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setString(1, customerName);
        ps.setString(2, Login.getCurrentUser());
        ps.setInt(3, customerId);
        ps.execute();

        //Get addressId
        String selectStatement = "Select * from customer WHERE customerId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setInt(1, customerId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            addressId = rs.getInt("addressId");
        }

        //Update Address
        updateStatement = "UPDATE address SET  address = ?, address2 = ?, postalCode = ?, phone = ?, " +
                "lastUpdate = now(), lastUpdateBy = ? WHERE addressId = ?";
        DBQuery.setPreparedStatement(conn, updateStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setString(1, address);
        ps.setString(2, address2);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setString(5, Login.getCurrentUser());
        ps.setInt(6, addressId);
        ps.execute();

        //Get cityId
        selectStatement = "SELECT * FROM address WHERE addressId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setInt(1, addressId);
        ps.execute();

        rs = ps.getResultSet();
        while(rs.next()){
            cityId = rs.getInt("cityId");
        }

        //Update City
        updateStatement = "UPDATE city SET city = ?, lastUpdate = now(), lastUpdateBy = ? WHERE cityId = ?";
        DBQuery.setPreparedStatement(conn, updateStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setString(1, city);
        ps.setString(2, Login.getCurrentUser());
        ps.setInt(3, cityId);
        ps.execute();

        //Get countryId
        selectStatement = "SELECT * FROM city WHERE cityId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setInt(1, cityId);
        ps.execute();

        rs = ps.getResultSet();
        while(rs.next()){
            countryId = rs.getInt("countryId");
        }

        //Update Country
        updateStatement = "UPDATE country SET country = ?, lastUpdate = now(), lastUpdateBy = ? WHERE countryId = ?";
        DBQuery.setPreparedStatement(conn, updateStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setString(1, country);
        ps.setString(2, Login.getCurrentUser());
        ps.setInt(3, countryId);
        ps.execute();
    }

    public static void deleteCustomer(int customerId) throws SQLException {
        int addressId = 0;

        //Est. connection
        Connection conn = DBConnection.getConnection();

        //Address Id
        String selectStatement = "Select * from customer WHERE customerId = ?";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.setInt(1, customerId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            addressId = rs.getInt("addressId");
        }

        //Delete Associated Appointments
        String deleteStatement = "DELETE FROM appointment WHERE customerId = ?";
        DBQuery.setPreparedStatement(conn, deleteStatement);
        ps= DBQuery.getPreparedStatement();

        ps.setInt(1, customerId);
        ps.execute();

        //Delete Customer
        deleteStatement = "DELETE FROM customer WHERE customerId = ?";
        DBQuery.setPreparedStatement(conn, deleteStatement);
        ps= DBQuery.getPreparedStatement();

        ps.setInt(1, customerId);
        ps.execute();

        //Delete Customer Address
        deleteStatement = "DELETE FROM address WHERE addressId = ?";
        DBQuery.setPreparedStatement(conn, deleteStatement);
        ps = DBQuery.getPreparedStatement();

        ps.setInt(1, addressId);
        ps.execute();
    }

    public static ObservableList<String> getCustomerList() throws SQLException {

        //Est. Connection
        Connection conn = DBConnection.getConnection();

        //Create Query
        String selectStatement = "SELECT * FROM customer";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();

        ps.execute();
        ResultSet rs = ps.getResultSet();


        while(rs.next()){
            int customerId = rs.getInt("customerId");
            String customerName = rs.getString("customerName");
            String  customerIdConverted = String.valueOf(customerId);
            String customerInformation = customerIdConverted + " - " + customerName;
            customerList.add(customerInformation);
        }
        return customerList;
    }

}
