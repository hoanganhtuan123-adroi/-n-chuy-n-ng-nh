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

    public boolean updateTour(TourModel tour) {
        String query = """
        UPDATE tours
        SET 
            package_id = ?, 
            tour_name = ?, 
            description = ?, 
            duration = ?, 
            start_date = ?, 
            end_date = ?, 
            destination = ?, 
            departure_location = ?, 
            capacity = ?, 
            available_seats = ?, 
            prices = ?, 
            updated_at = NOW()
        WHERE 
            tour_id = ?
    """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            // Thiết lập các tham số cho PreparedStatement
            pstmt.setInt(1, tour.getPackage_id());
            pstmt.setString(2, tour.getTour_name());
            pstmt.setString(3, tour.getDescription());
            pstmt.setInt(4, tour.getDuration());
            pstmt.setDate(5, new java.sql.Date(tour.getStart_date().getTime()));
            pstmt.setDate(6, new java.sql.Date(tour.getEnd_date().getTime()));
            pstmt.setString(7, tour.getDestination());
            pstmt.setString(8, tour.getDeparture_location());
            pstmt.setInt(9, tour.getCapacity());
            pstmt.setInt(10, tour.getAvailable_seats());
            pstmt.setInt(11, tour.getPrices());
            pstmt.setInt(12, tour.getTour_id());

            // Thực hiện cập nhật
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTour(int tourId) throws SQLException {
        String query ="DELETE FROM tours where tour_id = ?";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query) ){
            pstmt.setInt(1, tourId);
            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean addNewTour(TourModel tour) throws SQLException {
//        TourModel tour = tours;
        String query = "INSERT INTO tours (package_id, tour_name, description, duration, start_date, end_date, destination, departure_location, capacity, available_seats, prices, created_at, updated_at) \n" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query) ){
            pstmt.setInt(1, tour.getPackage_id());
            pstmt.setString(2, tour.getTour_name());
            pstmt.setString(3, tour.getDescription());
            pstmt.setInt(4, tour.getDuration());
            pstmt.setDate(5, new java.sql.Date(tour.getStart_date().getTime()));
            pstmt.setDate(6, new java.sql.Date(tour.getEnd_date().getTime()));
            pstmt.setString(7, tour.getDestination());
            pstmt.setString(8, tour.getDeparture_location());
            pstmt.setInt(9, tour.getCapacity());
            pstmt.setInt(10, tour.getAvailable_seats());
            pstmt.setInt(11, tour.getPrices());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        };
    return false;
    }

    public List<TourModel> searchToursByName(String tourName) throws SQLException {
        List<TourModel> tours = new ArrayList<>();
        String query = "SELECT t.tour_id, t.package_id, t.tour_name, t.destination, t.start_date, t.end_date, "
                + "t.departure_location, t.prices, p.package_name "
                + "FROM tours t "
                + "LEFT JOIN packages p ON t.package_id = p.package_id "
                + "WHERE t.tour_name LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, "%" + tourName + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int tourId = rs.getInt("tour_id");
                int packageId = rs.getInt("package_id");
                String name = rs.getString("tour_name");
                String destination = rs.getString("destination");
                Date startDate = rs.getDate("start_date");
                Date endDate = rs.getDate("end_date");
                String departureLocation = rs.getString("departure_location");
                int prices = rs.getInt("prices");
                String packageName = rs.getString("package_name");

                tours.add(new TourModel(tourId, packageId, name, destination, startDate, endDate, departureLocation, prices, packageName));
            }
        }
        return tours;
    }

}
