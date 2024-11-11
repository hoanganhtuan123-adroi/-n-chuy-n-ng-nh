package controller;

import database.DatabaseConnection;
import model.CustomerModel;
import model.TourModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourController {
    public List<TourModel> getAllTours() throws SQLException {
        List<TourModel> tours = new ArrayList<>();
        String query = """
        SELECT 
            t.tour_id, t.package_id, t.tour_name, t.description, t.duration,
            t.start_date, t.end_date, t.destination, t.departure_location,
            t.capacity, t.available_seats, t.prices, t.image_url, t.created_at,
            p.package_name, p.description AS package_description
        FROM tours t
        LEFT JOIN packages p ON t.package_id = p.package_id
    """;

        try (Connection con = DatabaseConnection.getConnection();
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(query)) {

            while (rs.next()) {
                // Lấy thông tin từ bảng `tours`
                int tourId = rs.getInt("tour_id");
                int packageId = rs.getInt("package_id");
                String tourName = rs.getString("tour_name");
                String tourDescription = rs.getString("description");
                int duration = rs.getInt("duration");
                Date startDate = rs.getDate("start_date");
                Date endDate = rs.getDate("end_date");
                String destination = rs.getString("destination");
                String departureLocation = rs.getString("departure_location");
                int capacity = rs.getInt("capacity");
                int availableSeats = rs.getInt("available_seats");
                int prices = rs.getInt("prices");
                String imageUrl = rs.getString("image_url");
                Timestamp createdAt = rs.getTimestamp("created_at");

                // Lấy thông tin từ bảng `packages`
                String packageName = rs.getString("package_name");
                String packageDescription = rs.getString("package_description");

                // Tạo đối tượng TourModel
                TourModel tour = new TourModel(
                        tourId, packageId, tourName, tourDescription, duration,
                        startDate, endDate, destination, departureLocation,
                        capacity, availableSeats, prices, imageUrl,
                        createdAt, packageName, packageDescription
                );

                // Thêm vào danh sách
                tours.add(tour);
            }
        }
        return tours;
    }

    public TourModel  showTourDetails(int tourId) {
        TourModel  tours = null;
        String query = "SELECT t.tour_id, t.package_id, t.tour_name, t.description, t.duration,\n" +
                "            t.start_date, t.end_date, t.destination, t.departure_location,\n" +
                "            t.capacity, t.available_seats, t.prices, t.image_url, t.created_at,\n" +
                "            p.package_name, p.description AS package_description " +
                "FROM tours t " +
                "LEFT JOIN packages p ON t.package_id = p.package_id " +
                "WHERE t.tour_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, tourId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int tour_Id = rs.getInt("tour_id");
                int packageId = rs.getInt("package_id");
                String tourName = rs.getString("tour_name");
                String tourDescription = rs.getString("description");
                int duration = rs.getInt("duration");
                Date startDate = rs.getDate("start_date");
                Date endDate = rs.getDate("end_date");
                String destination = rs.getString("destination");
                String departureLocation = rs.getString("departure_location");
                int capacity = rs.getInt("capacity");
                int availableSeats = rs.getInt("available_seats");
                int prices = rs.getInt("prices");
                String imageUrl = rs.getString("image_url");
                Timestamp createdAt = rs.getTimestamp("created_at");

                // Lấy thông tin từ bảng `packages`
                String packageName = rs.getString("package_name");
                String packageDescription = rs.getString("package_description");

                // Tạo đối tượng TourModel
               tours = new TourModel(
                        tourId, packageId, tourName, tourDescription, duration,
                        startDate, endDate, destination, departureLocation,
                        capacity, availableSeats, prices, imageUrl,
                        createdAt, packageName, packageDescription
                );


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tours;
    }

}
