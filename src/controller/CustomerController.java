package controller;

import database.DatabaseConnection;
import model.CustomerModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerController {

    public List<CustomerModel> getAllCustomers() throws SQLException {
        List<CustomerModel> customer = new ArrayList<>();
        try( Connection con = DatabaseConnection.getConnection()){
            String query = "SELECT * FROM customers";
            try(Statement stm = con.createStatement()){
                ResultSet rs = stm.executeQuery(query);
                while (rs.next()) { // Changed from if to while
                    String i = rs.getString("customer_id");
                    String ac_i = rs.getString("account_id");
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

    public boolean updateCustomer(CustomerModel customer) {
        boolean isUpdated = false;
        String query = "UPDATE customers SET name = ?, email = ?, phone = ?, address = ? WHERE customer_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, customer.getCustomer_name());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getCustomer_id());
            pstmt.executeUpdate();
            isUpdated = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    public boolean deleteCustomer(String customerID) {
        boolean isDeleted = false;
        String updatePayment = "UPDATE payments p\n" +
                "JOIN customers c ON p.customer_id = c.customer_id\n" +
                "SET p.customer_temp = c.name\n" +
                "WHERE c.customer_id = ?;\n";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmtt = conn.prepareStatement(updatePayment)) {
            pstmtt.setString(1, customerID);
            pstmtt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String query = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, customerID);
            pstmt.executeUpdate();
            isDeleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }


    public boolean addCustomer(CustomerModel customer) {
        String accountID = "ACC" + "_" + UUID.randomUUID().toString().substring(0, 5);
        String query = "INSERT INTO accounts(account_id, username, password, email, phone, role, status) VALUES(?, ?,'123456', ?,?, 'customer', 'active')";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement acc_pstmt = con.prepareStatement(query)) {
            con.setAutoCommit(false);
            acc_pstmt.setString(1, accountID);
            acc_pstmt.setString(2, customer.getCustomer_name().replaceAll(" ", "").toLowerCase()+"account");
            acc_pstmt.setString(3, customer.getEmail());
            acc_pstmt.setString(4, customer.getPhone());
            acc_pstmt.executeUpdate();

            String customerID = "CUS"+ "_" + UUID.randomUUID().toString().substring(0, 5);
            String customer_query = "INSERT INTO customers (customer_id, account_id, name, email, phone, address)\n" +
                    "VALUES(?, ?,?,?,?,?)";
            try (PreparedStatement cus_pstmt = con.prepareStatement(customer_query)) {
                cus_pstmt.setString(1, customerID);
                cus_pstmt.setString(2, accountID);
                cus_pstmt.setString(3, customer.getCustomer_name());
                cus_pstmt.setString(4, customer.getEmail());
                cus_pstmt.setString(5, customer.getPhone());
                cus_pstmt.setString(6, customer.getAddress());
                int row = cus_pstmt.executeUpdate();

                con.setAutoCommit(true);
                return row > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<CustomerModel> searchCustomerByFullName(String fullNameSearch) {
        List<CustomerModel> listCustomer = new ArrayList<>();
        String query = "SELECT customer_id, account_id, name, email, phone, address FROM customers WHERE name LIKE ?; ";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, "%" + fullNameSearch + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("customer_id");
                    String ac_id = rs.getString("account_id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    CustomerModel customer = new CustomerModel(id, ac_id, name, email,phone, address);
                    listCustomer.add(customer);
                }
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return listCustomer;
    }

    public CustomerModel getInformationCustomer(String customerID) throws SQLException {
        CustomerModel customer = null;
        String query = "SELECT name, email, phone, address FROM customers WHERE customer_id = ?;";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, customerID);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                customer = new CustomerModel(customerID, name, email, phone, address);
            }
        }
        return customer;
    }

}
