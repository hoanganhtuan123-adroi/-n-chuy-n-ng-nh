package controller;

import database.DatabaseConnection;
import model.CustomerModel;
import view.CustomerView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {
//    private CustomerModel model;
//    private CustomerView view;
//    public CustomerController(CustomerModel model, CustomerView view)  {
//        this.model = model;
//        this.view = view;
//    }

    public List<CustomerModel> getAllCustomers() throws SQLException {
        List<CustomerModel> customer = new ArrayList<>();
        try( Connection con = DatabaseConnection.getConnection()){
            String query = "SELECT * FROM customers";
            try(Statement stm = con.createStatement()){
                ResultSet rs = stm.executeQuery(query);
                while (rs.next()) { // Changed from if to while
                    int i = rs.getInt("customer_id");
                    int ac_i = rs.getInt("account_id");
                    String cname = rs.getString("name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    Timestamp created_at = rs.getTimestamp("created_at");
                    Timestamp updated_at = rs.getTimestamp("updated_at");
                    customer.add(new CustomerModel(i, ac_i, cname, email, phone, address, created_at, updated_at));
                }
            }
        }
        return customer;
    }

    public boolean updateCustomer(CustomerModel customer, int customerID) {
        String query = "UPDATE customers SET name = ?, email = ?, phone = ?, address = ? WHERE customer_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, customer.getCustomer_name());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getAddress());
            pstmt.setInt(5, customerID);
            System.out.println("id" + customerID);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("rows updated: " + rowsUpdated);
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
