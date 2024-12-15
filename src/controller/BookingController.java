package controller;

import database.DatabaseConnection;
import model.BookingModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingController {
    public List<BookingModel> getAllBookings() {
        List<BookingModel> bookings = new ArrayList<>();
        String query = "select b.booking_id, c.name, t.tour_name, b.booking_date, b.total_price, b.status from bookings as b\n" +
                "left join customers as c on c.customer_id = b.customer_id\n" +
                "left join tours as t on t.tour_id = b.tour_id;";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
             ResultSet rs = pstm.executeQuery();
             while(rs.next()) {
                 String booking_id = rs.getString("booking_id");
                 String cusName = rs.getString("name");
                 String tourName = rs.getString("tour_name");
                 String bookingDate = rs.getString("booking_date");
                 String totalPrice = rs.getString("total_price");
                 String status = rs.getString("status");
                 bookings.add(new BookingModel(booking_id,cusName,tourName,bookingDate,totalPrice,status));
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public BookingModel getBooking(int bookingID) throws SQLException {
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
            pstm.setInt(1, bookingID);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                String booking_id = rs.getString("booking_id");
                String cusName = rs.getString("name");
                String tourName = rs.getString("tour_name");
                String packageName = rs.getString("package_name");
                String bookingDate = rs.getString("booking_date");
                String numberPeople = rs.getString("number_of_people");
                String totalPrice = rs.getString("total_price");
                String status = rs.getString("status");
                String paymentStatus = rs.getString("payment_status");
                String paymentDate = rs.getString("payment_date");
                String specialRequests = rs.getString("special_requests");
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

    public boolean updateBooking(BookingModel booking) throws SQLException {
        boolean isSuccessful = false;
        String query = "update bookings as b\n" +
                "left join customers as c on c.customer_id = b.customer_id\n" +
                "left join tours as t on t.tour_id = b.tour_id\n" +
                "left join payments as py on py.booking_id = b.booking_id\n" +
                "set  c.name = ?, t.tour_name =?, b.booking_date = ?,\n" +
                "b.number_of_people = ?, b.status=?, py.status =?,\n" +
                "py.payment_date=?, b.special_requests=?,\n" +
                "b.total_price = b.number_of_people * t.prices where b.booking_id = ?;";
       try(Connection con = DatabaseConnection.getConnection();
       PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, booking.getCustomerName());
            pstm.setString(2, booking.getTourName());
            pstm.setDate(3, booking.getBookingDate());
            pstm.setInt(4, booking.getNumberOfPeople());
            pstm.setString(5, booking.getStatus());
            pstm.setString(6, booking.getPaymentStatus());
            pstm.setDate(7, booking.getPaymentDate());
            pstm.setString(8,booking.getRequests());
            pstm.setDouble(9,booking.getBookingID());
            pstm.executeUpdate();
           isSuccessful = true;
       }
       return isSuccessful;
    }


}
