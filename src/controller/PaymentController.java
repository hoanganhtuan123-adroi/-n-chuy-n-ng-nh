package controller;

import database.DatabaseConnection;
import model.PaymentModel;
import model.TourModel;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PaymentController {

    public List<PaymentModel> getAllPayment() {
        List<PaymentModel> list = new ArrayList<>();
        String query = "SELECT  DISTINCT p.payment_id, c.customer_id, c.name, " +
                "t.tour_name, p.payment_date, p.status, " +
                "IF(c.customer_id IS NULL, CONCAT('[DELETED] ', c.name), c.name) AS customer_display_name\n" +
                "FROM customers AS c " +
                "LEFT JOIN bookings AS b ON c.customer_id = b.customer_id " +
                "LEFT JOIN payments AS p ON b.booking_id = p.booking_id " +
                "LEFT JOIN tours AS t ON t.tour_id = b.tour_id;";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstm = con.prepareStatement(query)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                String payment_id = rs.getString("payment_id"); // Khi chưa có payment_id, bạn có thể bỏ cột này hoặc đặt 0.
                if(payment_id == null){
                    payment_id = "Chưa có giao dịch";
                }
                String customerName = rs.getString("name");

                // Nếu tour_name null thì set là "Chưa chọn"
                String tourName = rs.getString("tour_name");
                if (tourName == null) {
                    tourName = "Chưa chọn";
                }

                // Nếu payment_date null thì set là "chưa thanh toán"
                String paymentDateStr = rs.getString("payment_date");
                Date paymentDate;
                if (paymentDateStr == null) {
                    paymentDate = null; // Hoặc dùng một giá trị mặc định
                } else {
                    paymentDate = Date.valueOf(paymentDateStr);
                }

                // Nếu status null thì set là "Chưa thanh toán"
                String status = rs.getString("status");
                if (status == null) {
                    status = "Chưa thanh toán";
                }

                list.add(new PaymentModel(payment_id, customerName, tourName, paymentDate, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return list;
    }


    public String getCustomerIdByTourNameAndTourIdAndCusName(String tourName, int tourId, String customerName) {
        String id = "";
        String query = "SELECT c.customer_id " +
                "FROM customers AS c " +
                "LEFT JOIN bookings AS b ON c.customer_id = b.customer_id " +
                "LEFT JOIN tours AS t ON b.tour_id = t.tour_id " +
                "WHERE t.tour_name = ? AND t.tour_id = ? AND c.name = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, tourName);
            pstmt.setInt(2, tourId);
            pstmt.setString(3, customerName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    id = rs.getString("customer_id"); // Trả về customer_id nếu tìm thấy
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  return id;
    }

    public String getCustomerIdByCusName(String customerName) {
      String id = "";
        String query = "SELECT c.customer_id " +
                "FROM customers AS c " +
                "LEFT JOIN bookings AS b ON c.customer_id = b.customer_id " +
                "LEFT JOIN tours AS t ON b.tour_id = t.tour_id " +
                "WHERE c.name = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, customerName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    id = rs.getString("customer_id"); // Trả về customer_id nếu tìm thấy
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } return id;
    }

    public PaymentModel getPayment(String payment_id) throws SQLException {
        if(payment_id.equals("Chưa thanh toán")){
            return null;
        }
        PaymentModel payment = null;

        String query = "SELECT c.customer_id, c.name, " +
                "t.tour_name, p.payment_date, p.status, p.payment_id, p.payment_method, p.transaction_id, p.amount " +
                "FROM customers AS c " +
                "LEFT JOIN bookings AS b ON c.customer_id = b.customer_id " +
                "LEFT JOIN payments AS p ON b.booking_id = p.booking_id " +
                "LEFT JOIN tours AS t ON t.tour_id = b.tour_id WHERE p.payment_id = ?;";

        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1,payment_id);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                String paymentID = rs.getString("payment_id");
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

    public PaymentModel getPaymentByCustomerIDAndTourId(String customerId, int tourId) throws SQLException {
        String query = "SELECT \n" +
                "  c.customer_id, \n" +
                "  c.name, \n" +
                "  COALESCE(t.tour_name, 'Chưa chọn') AS tour_name,\n" +
                "  p.payment_date AS payment_date,\n" +
                "  COALESCE(p.status, 'Chưa thanh toán') AS status,\n" +
                "  COALESCE(p.payment_id, 'Chưa có giao dịch') AS payment_id,\n" +
                "  COALESCE(p.payment_method, 'N/A') AS payment_method,\n" +
                "  COALESCE(p.transaction_id, 'N/A') AS transaction_id,\n" +
                "  COALESCE(p.amount, '0') AS amount\n" +
                "FROM customers AS c\n" +
                "LEFT JOIN bookings AS b ON c.customer_id = b.customer_id\n" +
                "LEFT JOIN payments AS p ON b.booking_id = p.booking_id\n" +
                "LEFT JOIN tours AS t ON t.tour_id = b.tour_id\n" +
                "where c.customer_id = ? and t.tour_id = ?;";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstm = con.prepareStatement(query)) {
            pstm.setString(1, customerId);
            pstm.setInt(2, tourId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String paymentID = rs.getString("payment_id");
                    String customerName = rs.getString("name");
                    String tourName = rs.getString("tour_name");
                    String paymentDate = rs.getString("payment_date");
                    String amount = rs.getString("amount");
                    String status = rs.getString("status");
                    String paymentMethod = rs.getString("payment_method");
                    String transactionId = rs.getString("transaction_id");

                    java.sql.Date paymentDateSql = null;
                    if (paymentDate != null) {
                        paymentDateSql = java.sql.Date.valueOf(paymentDate); // Chuyển đổi nếu paymentDate không null
                    }

                    return new PaymentModel(paymentID, customerName, tourName, paymentDateSql, amount, status, paymentMethod, transactionId);

                }
            }
        }
        return null;
    }

    public PaymentModel getPaymentByCustomerIDAndTourNull(String customerId, String cusName) throws SQLException {
        String query = "SELECT \n" +
                "  c.customer_id, \n" +
                "  c.name, \n" +
                "  COALESCE(t.tour_name, 'Chưa chọn') AS tour_name,\n" +
                "  p.payment_date AS payment_date,\n" +
                "  COALESCE(p.status, 'Chưa thanh toán') AS status,\n" +
                "  COALESCE(p.payment_id, 'Chưa có giao dịch') AS payment_id,\n" +
                "  COALESCE(p.payment_method, 'N/A') AS payment_method,\n" +
                "  COALESCE(p.transaction_id, 'N/A') AS transaction_id,\n" +
                "  COALESCE(p.amount, '0') AS amount\n" +
                "FROM customers AS c\n" +
                "LEFT JOIN bookings AS b ON c.customer_id = b.customer_id\n" +
                "LEFT JOIN payments AS p ON b.booking_id = p.booking_id\n" +
                "LEFT JOIN tours AS t ON t.tour_id = b.tour_id\n" +
                "where c.customer_id = ? and c.name = ? and t.tour_id is null;";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstm = con.prepareStatement(query)) {
            pstm.setString(1, customerId);
            pstm.setString(2, cusName);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String paymentID = rs.getString("payment_id");
                    String customerName = rs.getString("name");
                    String tourName = rs.getString("tour_name");
                    String paymentDate = rs.getString("payment_date");
                    String amount = rs.getString("amount");
                    String status = rs.getString("status");
                    String paymentMethod = rs.getString("payment_method");
                    String transactionId = rs.getString("transaction_id");

                    java.sql.Date paymentDateSql = null;
                    if (paymentDate != null) {
                        paymentDateSql = java.sql.Date.valueOf(paymentDate); // Chuyển đổi nếu paymentDate không null
                    }

                    return new PaymentModel(paymentID, customerName, tourName, paymentDateSql, amount, status, paymentMethod, transactionId);

                }
            }
        }
        return null;
    }

    public boolean updatePayment(PaymentModel payment) throws SQLException {
    String query = "UPDATE payments AS p \n" +
            "LEFT JOIN customers AS c ON p.customer_id = c.customer_id\n" +
            "LEFT JOIN bookings AS b ON b.booking_id = p.booking_id\n" +
            "LEFT JOIN tours AS t ON t.tour_id = b.tour_id\n" +
            "SET c.name = ?, b.tour_id = ?, b.package_id = t.package_id, p.payment_date = ?,\n" +
            "p.amount = ?, p.payment_method = ?, p.transaction_id = ?, p.status=?\n" +
            "WHERE p.payment_id = ?;";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement pstm = con.prepareStatement(query)) {

        pstm.setString(1, payment.getCustomerName());    // c.name = ?
        pstm.setInt(2, payment.getTourId());             // b.tour_id = ?
        // b.package_id lấy từ t.package_id, không cần set param
        pstm.setDate(3, payment.getPaymentDate());       // p.payment_date = ?
        pstm.setString(4, payment.getAmount());          // p.amount = ?
        pstm.setString(5, payment.getPaymentMethod());   // p.payment_method = ?
        pstm.setString(6, payment.getTransactionID());   // p.transaction_id = ?
        pstm.setString(7, payment.getPaymentStatus());   // p.status = ?
        pstm.setString(8, payment.getPaymentID());          // WHERE p.payment_id = ?

        int row = pstm.executeUpdate();
        return row > 0;  // row > 0 nghĩa là có bản ghi được cập nhật
    }
}

    public boolean deletePayment(String id) throws SQLException {
        String query = "DELETE FROM payments WHERE payment_id = ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1,id);
            int row = pstm.executeUpdate();
            return row> 0;
        }
    }

    // Kiểm tra hoặc thêm mới Customer
    public String checkOrAddCustomer(Connection conn, String name, String email, String phone, String address) throws SQLException {
        String checkCustomerQuery = "SELECT customer_id FROM customers WHERE email = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkCustomerQuery)) {
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return rs.getString("customer_id"); // Nếu tồn tại, trả về customer_id
            } else {
                String customerID = "CUS" +"_"+ UUID.randomUUID().toString().substring(0, 5);
                // Nếu không tồn tại, thêm mới Customer
                String addCustomerQuery = "INSERT INTO customers (customer_id,name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement addStmt = conn.prepareStatement(addCustomerQuery, Statement.RETURN_GENERATED_KEYS)) {
                    addStmt.setString(1, customerID);
                    addStmt.setString(2, name);
                    addStmt.setString(3, email);
                    addStmt.setString(4, phone);
                    addStmt.setString(5, address);
                    addStmt.executeUpdate();

                    ResultSet resultSet = addStmt.getGeneratedKeys();
                    if (resultSet.next()) {
                        return resultSet.getString(1); // Trả về customer_id mới
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
    public  String addBooking(Connection conn, String customerId, int tourId, int packageId , int numberPeople ,double totalPrice, String paymentStatus) throws SQLException {
       String booking_id = "BOK_" + UUID.randomUUID().toString().substring(0, 5);
        String addBookingQuery = "INSERT INTO bookings (booking_id, customer_id, tour_id, package_id, booking_date, number_of_people,total_price, status, payment_status, " +
                "payment_date, special_requests ) " +
                "VALUES (?, ?, ?, ?, NOW(), ?, ?, 'pending', ?, NOW(), 'NO')";
        try (PreparedStatement stmt = conn.prepareStatement(addBookingQuery)) {
            stmt.setString(1, booking_id);
            stmt.setString(2, customerId);
            stmt.setInt(3, tourId);
            stmt.setDouble(4, packageId);
            stmt.setInt(5, numberPeople);
            stmt.setDouble(6, totalPrice);
            stmt.setString(7, paymentStatus);
            int rs = stmt.executeUpdate();

            if (rs > 0) {
                return booking_id; // Trả về booking_id mới
            }
        }
        throw new SQLException("Không thể thêm mới Booking!");
    }

    // Tạo transaction_id
    public  String generateTransactionId()  {
        return "TXN" + "_" + UUID.randomUUID().toString().substring(0, 5);
    }

    public boolean addPayment(Connection conn, String bookingId, String customerId, String transactionId, String paymentDate, String paymentMethod, String status, double amount) throws SQLException {
        Random r = new Random();
        String paymentId = "PYC" + r.nextInt(100 - 1) + r.nextInt(20 - 1) + customerId;
        String addPaymentQuery = "INSERT INTO payments (payment_id, customer_id, booking_id, payment_date, amount, payment_method, transaction_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(addPaymentQuery)) {
            stmt.setString(1, paymentId);
            stmt.setString(2, customerId);
            stmt.setString(3, bookingId);
            stmt.setDate(4, Date.valueOf(paymentDate));
            stmt.setDouble(5, amount);
            stmt.setString(6, paymentMethod);
            stmt.setString(7, transactionId);
            stmt.setString(8, status);
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
        String query = "SELECT c.customer_id, c.name, " +
                "t.tour_name, p.payment_date, p.status, p.payment_id " +
                "FROM customers AS c " +
                "LEFT JOIN bookings AS b ON c.customer_id = b.customer_id " +
                "LEFT JOIN payments AS p ON b.booking_id = p.booking_id " +
                "LEFT JOIN tours AS t ON t.tour_id = b.tour_id where c.name like ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, "%" + cusName + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String payment_id = rs.getString("payment_id"); // Khi chưa có payment_id, bạn có thể bỏ cột này hoặc đặt 0.
                if(payment_id == null){
                    payment_id = "Chưa có giao dịch";
                }
                String customerName = rs.getString("name");

                // Nếu tour_name null thì set là "Chưa chọn"
                String tourName = rs.getString("tour_name");
                if (tourName == null) {
                    tourName = "Chưa chọn";
                }

                // Nếu payment_date null thì set là "chưa thanh toán"
                String paymentDateStr = rs.getString("payment_date");
                Date paymentDate;
                if (paymentDateStr == null) {
                    paymentDate = null; // Hoặc dùng một giá trị mặc định
                } else {
                    paymentDate = Date.valueOf(paymentDateStr);
                }

                // Nếu status null thì set là "Chưa thanh toán"
                String status = rs.getString("status");
                if (status == null) {
                    status = "Chưa thanh toán";
                }

                payments.add(new PaymentModel(payment_id, customerName, tourName, paymentDate, status));
            }
        }
        return payments;
    }

    public double checkPrice(Connection con, int tourID) throws SQLException {
        double priceTour = 0;
        String query = "select prices from tours where tour_id = ?;";
        try(PreparedStatement ptmt = con.prepareStatement(query)){
            ptmt.setInt(1, tourID);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next()){
                priceTour = rs.getDouble("prices");
            }
        }
        return  priceTour;
    }
    
    public long checkPriceByTourName(Connection con, String tourName) throws SQLException {
        long priceTour = 0;
        String query = "select prices from tours where tour_name = ?;";
        try(PreparedStatement ptmt = con.prepareStatement(query)){
            ptmt.setString(1, tourName);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next()){
                priceTour = rs.getLong("prices");
            }
        }
        return  priceTour;
    }

    public String checkPaymentId(String customerID){
        String paymentID = "";
        String query = "select p.payment_id from payments as p \n" +
                "left join customers as c on c.customer_id = p.customer_id\n" +
                "where c.customer_id = ? ;";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, customerID);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                paymentID = rs.getString("payment_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return paymentID;
    }

}
