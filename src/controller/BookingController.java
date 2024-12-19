package controller;

import database.DatabaseConnection;
import model.BookingModel;
import model.CustomerModel;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BookingController {

    public List<BookingModel> getAllBookings() {
        List<BookingModel> bookings = new ArrayList<>();

        // Câu lệnh UPDATE để gán t.prices vào b.total_price
        String updateQuery = "UPDATE bookings AS b " +
                "INNER JOIN tours AS t ON t.tour_id = b.tour_id " +
                "SET b.total_price = t.prices";

        // Câu lệnh SELECT để lấy danh sách đặt chỗ
        String selectQuery = "SELECT b.booking_id, c.name, t.tour_name, b.booking_date, b.total_price, b.status " +
                "FROM bookings AS b " +
                "INNER JOIN customers AS c ON c.customer_id = b.customer_id " +
                "INNER JOIN tours AS t ON t.tour_id = b.tour_id";

        try (Connection con = DatabaseConnection.getConnection()) {
            // Thực hiện UPDATE
            try (PreparedStatement updatePstm = con.prepareStatement(updateQuery)) {
                updatePstm.executeUpdate();
            }

            // Thực hiện SELECT
            try (PreparedStatement selectPstm = con.prepareStatement(selectQuery)) {
                ResultSet rs = selectPstm.executeQuery();
                while (rs.next()) {
                    String booking_id = rs.getString("booking_id");
                    String cusName = rs.getString("name");
                    String tourName = rs.getString("tour_name");
                    String bookingDate = rs.getString("booking_date");
                    String totalPrice = rs.getString("total_price");
                    String status = rs.getString("status");

                    bookings.add(new BookingModel(booking_id, cusName, tourName, bookingDate, totalPrice, status));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public BookingModel getBooking(String bookingID) throws SQLException {
        BookingModel bookings = null;
        String query = "select b.booking_id, c.name, t.tour_name, p.package_name, b.booking_date, b.number_of_people, \n" +
                "b.total_price, b.status,  py.status as payment_status, py.payment_date, b.special_requests from bookings as b\n" +
                "left join customers as c on c.customer_id = b.customer_id\n" +
                "left join tours as t on t.tour_id = b.tour_id\n" +
                "left join packages as p on p.package_id = b.package_id\n" +
                "left join payments as py on py.booking_id = b.booking_id " +
                "where b.booking_id = ?;";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, bookingID);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                String booking_id = rs.getString("booking_id");
                String cusName = rs.getString("name");
                String tourName = rs.getString("tour_name");
                String packageName = rs.getString("package_name");
                String bookingDate  = rs.getString("booking_date");
                String numberPeople = rs.getString("number_of_people");
                String totalPrice = rs.getString("total_price");
                String status = rs.getString("status");
                String paymentStatus = rs.getString("payment_status");
                String paymentDate = rs.getString("payment_date");
                String specialRequests = rs.getString("special_requests");

                System.out.println(paymentStatus);
                System.out.println(paymentDate);

                bookings = new BookingModel(booking_id,cusName, tourName,packageName,bookingDate,numberPeople,
                        totalPrice, status, paymentStatus,paymentDate,specialRequests );
            }
        }  return bookings;
    }

    public List<String> getListTours() throws SQLException {
        List<String> listTours = new ArrayList<>();
        String query = "SELECT tour_name from tours";
        try(Connection con = DatabaseConnection.getConnection();
                PreparedStatement pstm = con.prepareStatement(query)){
            pstm.executeQuery();
            ResultSet rs = pstm.getResultSet();
            while (rs.next()) {
                listTours.add(rs.getString("tour_name"));
            }
            return listTours;
        }
    }

    public List<BookingModel> searchByName(String name){
        List<BookingModel> lists = new ArrayList<>();
        String query = "select b.booking_id, c.name, t.tour_name, b.booking_date, b.total_price, b.status from bookings as b\n" +
                "left join customers as c on c.customer_id = b.customer_id\n" +
                "left join tours as t on t.tour_id = b.tour_id where c.name like ?;";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, "%"+name+"%");
            ResultSet rs = pstm.executeQuery();
            while(rs.next()) {
                String booking_id = rs.getString("booking_id");
                String cusName = rs.getString("name");
                String tourName = rs.getString("tour_name");
                String bookingDate = rs.getString("booking_date");
                String totalPrice = rs.getString("total_price");
                String status = rs.getString("status");
                lists.add(new BookingModel(booking_id,cusName,tourName,bookingDate,totalPrice,status));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lists;
    }

    public int getTourID(String tourName) throws SQLException {
        int tourID = 0;
        String query = "Select tour_id from tours where tour_name = ?";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, tourName);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                tourID = rs.getInt("tour_id");
            }
        }
        return tourID;
    }

    public int getPackageID(int tourID) throws SQLException {
        int packageID = 0;
        String query = "Select package_id from tours where tour_id = ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setInt(1, tourID);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                packageID = rs.getInt("package_id");
            }
        }return packageID;
    }

    public boolean updateBooking(BookingModel booking) throws SQLException {
        int tourID = getTourID(booking.getTourName());
        int packageID = getPackageID(tourID);
        boolean isSuccessful = false;
        String query = "UPDATE bookings AS b " +
                "LEFT JOIN customers AS c ON c.customer_id = b.customer_id " +
                "LEFT JOIN tours AS t ON t.tour_id = b.tour_id " +
                "LEFT JOIN payments AS py ON py.booking_id = b.booking_id " +
                "SET b.tour_id = ?, " +
                "b.booking_date = ?, " +
                "b.package_id = ?, " +
                "b.number_of_people = ?, " +
                "b.special_requests = ? ," +
                "b.payment_status = ? ," +
                "py.status = ? ," +
                "b.status = ? " +
                "WHERE b.booking_id = ?;";

        try(Connection con = DatabaseConnection.getConnection();
       PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setInt(1, tourID);
            pstm.setString(2, booking.getBookingDate());
            pstm.setInt(3, packageID);
            pstm.setInt(4, booking.getNumberOfPeople());
            pstm.setString(5, booking.getRequests());
            pstm.setString(6, booking.getPaymentStatus());
            pstm.setString(7, booking.getPaymentStatus());
            pstm.setString(8, booking.getStatus());
            pstm.setString(9, booking.getBookingID());
            pstm.executeUpdate();
           isSuccessful = true;
       }
       return isSuccessful;
    }

    public boolean deleteBooking(String bookingID) throws SQLException {
        String query = "DELETE FROM bookings WHERE booking_id = ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)) {
            pstm.setString(1, bookingID);
            int rowsUpdated = pstm.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public String addCustomer(BookingModel bookings) {
        String accountID = "ACC" + "_" + UUID.randomUUID().toString().substring(0, 5);
        String query = "INSERT INTO accounts(account_id, username, password, email, phone, role, status) VALUES(?, ?,'123456', ?,?, 'customer', 'active')";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement acc_pstmt = con.prepareStatement(query)) {
            con.setAutoCommit(false);
            acc_pstmt.setString(1, accountID);
            acc_pstmt.setString(2, bookings.getCustomerName().replaceAll(" ", "").toLowerCase()+"account");
            acc_pstmt.setString(3, bookings.getCustomerEmail());
            acc_pstmt.setString(4, bookings.getCustomerPhone());
            acc_pstmt.executeUpdate();

            String customerID = "CUS"+ "_" + UUID.randomUUID().toString().substring(0, 5);
            String customer_query = "INSERT INTO customers (customer_id, account_id, name, email, phone, address)\n" +
                    "VALUES(?, ?,?,?,?,?)";
            try (PreparedStatement cus_pstmt = con.prepareStatement(customer_query)) {
                cus_pstmt.setString(1, customerID);
                cus_pstmt.setString(2, accountID);
                cus_pstmt.setString(3, bookings.getCustomerName());
                cus_pstmt.setString(4, bookings.getCustomerEmail());
                cus_pstmt.setString(5, bookings.getCustomerPhone());
                cus_pstmt.setString(6, bookings.getCustomerAddress());
                cus_pstmt.executeUpdate();

                con.setAutoCommit(true);
                return customerID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean addBooking(BookingModel bookings, int packageID, String customerID, int tourID) throws SQLException {
        boolean isSuccessful = false;

        String bookingId = "BKG" + "_" + UUID.randomUUID().toString().substring(0, 5);
        String query = "INSERT INTO bookings (\n" +
                "    booking_id, tour_id, package_id, booking_date, number_of_people, total_price,\n" +
                "    status, payment_status, payment_date, special_requests, created_at, updated_at, customer_id\n" +
                ") VALUES (\n" +
                "    ?, ?, ?, ?, ?, ?,\n" +
                "    ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?\n" +
                ");\n";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, bookingId);
            pstm.setInt(2, tourID);
            pstm.setInt(3, packageID);
            pstm.setString(4, bookings.getBookingDate());
            pstm.setInt(5, bookings.getNumberOfPeople());
            pstm.setString(6, bookings.getTourPrice());
            pstm.setString(7, bookings.getStatus());
            pstm.setString(8, bookings.getPaymentStatus());
            pstm.setString(9, bookings.getPaymentDate());
            pstm.setString(10, bookings.getRequests());
            pstm.setString(11, customerID);
            int rowAffected = pstm.executeUpdate();
            if(rowAffected > 0){
                if(addPayment(bookings,bookingId, customerID)) isSuccessful = true;
            }
        }

        return isSuccessful;
    }

    public boolean addPayment(BookingModel bookings, String bookingID, String customerID) throws SQLException {
        String transactionId = "TXN" + "_" + UUID.randomUUID().toString().substring(0, 5);
        String paymentId = "PYC" +"_" + UUID.randomUUID().toString().substring(0, 5);
        String addPaymentQuery = "INSERT INTO payments (payment_id, customer_id, booking_id, payment_date, amount, payment_method, transaction_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(addPaymentQuery)) {
            stmt.setString(1, paymentId);
            stmt.setString(2, customerID);
            stmt.setString(3, bookingID);
            stmt.setString(4, bookings.getBookingDate());
            stmt.setString(5, bookings.getDeposit());
            stmt.setString(6, "Chuyển Khoản");
            stmt.setString(7, transactionId);
            stmt.setString(8, bookings.getPaymentStatus());
            int row = stmt.executeUpdate();
            return row > 0 ;
        }
    }

    public double checkPrice(int tourID) throws SQLException {
        double priceTour = 0;
        String query = "select prices from tours where tour_id = ?;";
        try(Connection con = DatabaseConnection.getConnection()
                ;PreparedStatement ptmt = con.prepareStatement(query)){
            ptmt.setInt(1, tourID);
            ResultSet rs = ptmt.executeQuery();
            if(rs.next()){
                priceTour = rs.getDouble("prices");
            }
        }
        return  priceTour;
    }

    public double checkAmountAtPayments(String bookingID) throws SQLException {
        double deposit = 0;
        String queyr = "select amount from payments where booking_id = ?;";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(queyr)){
            pstm.setString(1, bookingID);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                deposit = rs.getDouble("amount");
            }
        }
        return deposit;
    }

    public String compareTotalPriceAndAmount(String bookingId) {
        String paymentStatus = "Chưa thanh toán đủ"; // Mặc định là chưa thanh toán đủ

        // Truy vấn để lấy total_price từ bảng bookings và amount từ bảng payments theo booking_id
        String query = "SELECT b.total_price, p.amount " +
                "FROM bookings b " +
                "JOIN payments p ON b.booking_id = p.booking_id " +
                "WHERE b.booking_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, bookingId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                double totalPrice = rs.getDouble("total_price");
                double amountPaid = rs.getDouble("amount");

                // So sánh total_price và amount
                if (totalPrice == amountPaid) {
                    paymentStatus = "Đã thanh toán";
                } else if (amountPaid >= totalPrice / 2) {
                    paymentStatus = "Đã cọc 50%";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paymentStatus;
    }

}
