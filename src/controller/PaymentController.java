package controller;

import database.DatabaseConnection;
import model.PaymentModel;
import model.TourModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentController {

    public List<PaymentModel> getAllPayment(){
        List<PaymentModel> list = new ArrayList<PaymentModel>();
        String query = " select p.payment_id, c.name, t.tour_name ,p.payment_date, p.status from payments as p \n" +
                "left join customers as c on c.customer_id = p.customer_id\n" +
                "left join bookings as b on b.booking_id = p.booking_id\n" +
                "left join tours as t on t.tour_id = b.tour_id; ";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int payment_id = rs.getInt("payment_id");
                String customerName = rs.getString("name");
                String tourName = rs.getString("tour_name");
                String paymentDate = rs.getString("payment_date");
                String status = rs.getString("status");
                list.add(new PaymentModel(payment_id,customerName,tourName, Date.valueOf(paymentDate),status ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }

    public PaymentModel getPayment(int payment_id) throws SQLException {
        PaymentModel payment = null;
        String query = "SELECT p.payment_id, c.name, t.tour_name," +
                " p.payment_date, p.amount, p.payment_method, " +
                "p.transaction_id, p.status " +
                "FROM payments AS p " +
                "LEFT JOIN customers AS c ON c.customer_id = p.customer_id " +
                "LEFT JOIN bookings AS b ON b.booking_id = p.booking_id " +
                "LEFT JOIN tours AS t ON t.tour_id = b.tour_id " +
                "WHERE p.payment_id = ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setInt(1,payment_id);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                int paymentID = rs.getInt("payment_id");
                String customerName = rs.getString("name");
                String tourName = rs.getString("tour_name");
                String paymentDate = rs.getString("payment_date");
                String amount = rs.getString("amount");
                String status = rs.getString("status");
                String paymentMethod = rs.getString("payment_method");
                String transactionId = rs.getString("transaction_id");
                payment = new PaymentModel(paymentID, customerName, tourName, Date.valueOf(paymentDate), amount, status, paymentMethod, transactionId);
            }
            con.close();
            return payment;
        }
    }

    public boolean updatePayment(PaymentModel payment) throws SQLException {

        String query = "UPDATE payments AS p \n" +
                "LEFT JOIN customers AS c ON p.customer_id = c.customer_id\n" +
                "LEFT JOIN bookings AS b ON b.booking_id = p.booking_id\n" +
                "LEFT JOIN tours AS t ON t.tour_id = b.booking_id\n" +
                "SET c.name = ?, t.tour_name = ?, p.payment_date = ?,\n" +
                "p.amount = ?, p.payment_method = ?, p.transaction_id = ?, p.status=?\n" +
                "WHERE p.payment_id = ?;";

        try(Connection con = DatabaseConnection.getConnection()){

            try(PreparedStatement pstm = con.prepareStatement(query)){
                pstm.setString(1, payment.getCustomerName());
                pstm.setString(2, payment.getTourName());
                pstm.setDate(3, payment.getPaymentDate());
                pstm.setString(4, payment.getAmount());
                pstm.setString(5, payment.getPaymentMethod());
                pstm.setString(6, payment.getTransactionID());
                pstm.setString(7, payment.getPaymentStatus());
                pstm.setInt(8,payment.getPaymentID());
                int row = pstm.executeUpdate();
                con.close();
                return row >=0;
            }
        }
    }

    public boolean deletePayment(int id) throws SQLException {
        String query = "DELETE FROM payments WHERE payment_id = ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setInt(1,id);
            int row = pstm.executeUpdate();
            return row> 0;
        }
    }

    // Kiểm tra hoặc thêm mới Customer
    public int checkOrAddCustomer(Connection conn, String name, String email, String phone, String address) throws SQLException {
        String checkCustomerQuery = "SELECT customer_id FROM customers WHERE email = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkCustomerQuery)) {
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("customer_id"); // Nếu tồn tại, trả về customer_id
            } else {
                // Nếu không tồn tại, thêm mới Customer
                String addCustomerQuery = "INSERT INTO customers (name, email, phone, address) VALUES (?, ?, ?, ?)";
                try (PreparedStatement addStmt = conn.prepareStatement(addCustomerQuery, Statement.RETURN_GENERATED_KEYS)) {
                    addStmt.setString(1, name);
                    addStmt.setString(2, email);
                    addStmt.setString(3, phone);
                    addStmt.setString(4, address);
                    addStmt.executeUpdate();

                    ResultSet resultSet = addStmt.getGeneratedKeys();
                    if (resultSet.next()) {
                        return resultSet.getInt(1); // Trả về customer_id mới
                    }
                }
            }

            throw new SQLException("Không thể thêm mới Customer!");
        }

    }

    // Lấy tour_id từ tên Tour
    public  int getTourIdByName(Connection conn, String tourName) throws SQLException {
        String query = "SELECT tour_id FROM tours WHERE tour_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tourName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("tour_id");
            }
        }
        throw new SQLException("Không tìm thấy tour với tên: " + tourName);
    }

    public int getPackageIdByTourID(Connection con, int tourID)  throws SQLException{
        String query = "SELECT package_id FROM tours WHERE tour_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, tourID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("package_id");
            }
        }
        throw new SQLException("Không tìm thấy package ID");
    }

    // Thêm mới Booking
    public  int addBooking(Connection conn, int customerId, int tourId, int packageId , int numberPeople ,double totalPrice) throws SQLException {
        String addBookingQuery = "INSERT INTO bookings (customer_id, tour_id, package_id, booking_date, number_of_people,total_price, status, payment_status, " +
                "payment_date, special_requests ) " +
                "VALUES (?, ?, ?, NOW(), ?, ?, 'pending', 'paid', NOW(), 'NO')";
        try (PreparedStatement stmt = conn.prepareStatement(addBookingQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, tourId);
            stmt.setDouble(3, packageId);
            stmt.setInt(4, numberPeople);
            stmt.setDouble(5, totalPrice);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Trả về booking_id mới
            }
        }
        throw new SQLException("Không thể thêm mới Booking!");
    }

    // Tạo transaction_id
    public  String generateTransactionId(Connection conn) throws SQLException {
        String getMaxTransactionIdQuery = "SELECT MAX(CAST(SUBSTRING(transaction_id, 8) AS UNSIGNED)) AS max_id FROM payments";
        int nextId = 1;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(getMaxTransactionIdQuery)) {

            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                nextId = maxId + 1;
            }
        }
        return "TXN1234" + nextId;
    }

    public boolean addPayment(Connection conn, int bookingId, int customerId, String transactionId, String paymentDate, String paymentMethod, String status, double amount) throws SQLException {
        String addPaymentQuery = "INSERT INTO payments (customer_id, booking_id, payment_date, amount, payment_method, transaction_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(addPaymentQuery)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, bookingId);
            stmt.setDate(3, Date.valueOf(paymentDate));
            stmt.setDouble(4, amount);
            stmt.setString(5, paymentMethod);
            stmt.setString(6, transactionId);
            stmt.setString(7, status);
            int row = stmt.executeUpdate();
            return row > 0 ;
        }
    }

    public List<String> getListTours(Connection con) throws SQLException {
        List<String> listTours = new ArrayList<>();
        String query = "SELECT tour_name from tours";
        try(PreparedStatement pstm = con.prepareStatement(query)){
            pstm.executeQuery();
            ResultSet rs = pstm.getResultSet();
            while (rs.next()) {
                listTours.add(rs.getString("tour_name"));
            }
            return listTours;
        }
    }

    public List<PaymentModel> searchPaymentByCusName(String cusName) throws SQLException {
        List<PaymentModel> payments = new ArrayList<>();
        String query = " select p.payment_id, c.name, t.tour_name ,p.payment_date, p.status from payments as p \n" +
                "left join customers as c on c.customer_id = p.customer_id\n" +
                "left join bookings as b on b.booking_id = p.booking_id\n" +
                "left join tours as t on t.tour_id = b.tour_id where c.name like ?;";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, "%" + cusName + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int payment_id = rs.getInt("payment_id");
                String customerName = rs.getString("name");
                String tourName = rs.getString("tour_name");
                String paymentDate = rs.getString("payment_date");
                String status = rs.getString("status");
                payments.add(new PaymentModel(payment_id,customerName,tourName, Date.valueOf(paymentDate),status ));
            }
        }
        return payments;
    }


}
