package Customer;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DBConnection;
import utils.DBQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerTable {

    private int customerId;
    private String customerName;
    public static ObservableList<CustomerTable> customerList = FXCollections.observableArrayList();


    public CustomerTable(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

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

    // List of customers
    public static ObservableList<CustomerTable> getCustomerList() throws SQLException {

            //Est. connection
            Connection conn = DBConnection.getConnection();

            //Create Customer List
            String selectStatement = "SELECT * FROM customer";
            DBQuery.setPreparedStatement(conn, selectStatement);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();

            ResultSet rs = ps.getResultSet();

            while(rs.next()) {
                customerList.add(new CustomerTable(rs.getInt("customerId"),
                        rs.getString("customerName")));
            }
        return customerList;
    }
}
