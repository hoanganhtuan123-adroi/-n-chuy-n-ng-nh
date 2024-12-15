package controller;

import database.DatabaseConnection;
import model.CustomerModel;
import model.TourModel;

import javax.xml.transform.Result;
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
        String query = "SELECT \n" +
"    t.tour_id, \n" +
"    t.package_id, \n" +
"    t.tour_name, \n" +
"    t.description, \n" +
"    t.duration,\n" +
"    t.start_date, \n" +
"    t.end_date, \n" +
"    t.destination, \n" +
"    t.departure_location,\n" +
"    t.capacity, \n" +
"    t.available_seats, \n" +
"    t.prices, \n" +
"    t.image_url, \n" +
"    t.created_at,\n" +
"    p.package_name, \n" +
"    p.description AS package_description, \n" +
"    GROUP_CONCAT(s.service_name ORDER BY s.service_name SEPARATOR ', ') AS service_names\n" +
"FROM tours t \n" +
"LEFT JOIN packages p ON t.package_id = p.package_id \n" +
"LEFT JOIN services s ON s.package_id = p.package_id\n" +
"WHERE t.tour_id = ?\n" +
"GROUP BY t.tour_id, t.package_id, t.tour_name, t.description, t.duration,"
                + " t.start_date, t.end_date, t.destination, t.departure_location,"
                + " t.capacity, t.available_seats, t.prices, t.image_url, t.created_at, "
                + "p.package_name, p.description;";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, tourId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int tour_Id = rs.getInt("tour_id");
                int package_id = rs.getInt("package_id");
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

                // Lấy thông tin dịch vụ trong package
                String serviceName = rs.getString("service_names");
                
                // Tạo đối tượng TourModel
               tours = new TourModel(
                        tourId, package_id, tourName, tourDescription, duration,
                        startDate, endDate, destination, departureLocation,
                        capacity, availableSeats, prices, imageUrl,
                        createdAt, packageName, packageDescription, serviceName
                );


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tours;
    }

    public boolean updateTour(TourModel tour) throws SQLException {
        int packageID = 0;
        String queryPackID = "Select package_id from packages where package_name = ?";
        try(Connection con1 = DatabaseConnection.getConnection();
            PreparedStatement ps = con1.prepareStatement(queryPackID)){
            ps.setString(1, tour.getPackageName());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                packageID = rs.getInt("package_id");
            }
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
                pstmt.setInt(1, packageID);
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


    }

    public boolean deleteTour(int tourId) throws SQLException {
        boolean success = false;
        String query ="DELETE FROM tours where tour_id = ?";
        String getMaxIdQuery = "SELECT MAX(tour_id) FROM tours";
        String alterQuery = "ALTER TABLE tours AUTO_INCREMENT = ?";

        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            Statement stmt = con.createStatement();){
            pstmt.setInt(1, tourId);
            pstmt.executeUpdate();

            ResultSet rs = stmt.executeQuery(getMaxIdQuery);
            int maxId = 0;
            if (rs.next()) {
                maxId = rs.getInt(1);  // Lấy giá trị MAX(issue_id)
            }

            try (PreparedStatement alterPstm = con.prepareStatement(alterQuery)) {
                alterPstm.setInt(1, maxId + 1);  // Cập nhật AUTO_INCREMENT là MAX + 1
                alterPstm.executeUpdate();
            }


            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return success;
    }

    public boolean addNewTour(TourModel tour) throws SQLException {
        boolean isSuccess = false;
        String getIDPack = "SELECT package_id FROM packages WHERE package_name = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(getIDPack)) {

            pst.setString(1, tour.getPackageName());
            int packageID = 0;
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    packageID = rs.getInt("package_id");
                }
            }

            // Nếu không tìm thấy packageID, trả về false ngay
            if (packageID == 0) {
                return false;
            }

            String query = "INSERT INTO tours (package_id, tour_name, description, duration, start_date, end_date, " +
                    "destination, departure_location, capacity, available_seats, prices, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, packageID);
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
                if (rowsAffected > 0) {
                    isSuccess = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Error while inserting the tour.", e); // Ném lại ngoại lệ
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while retrieving the package ID.", e); // Ném lại ngoại lệ
        }

        return isSuccess;
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

    public List<TourModel> searchToursByDestination(String destinationName) throws SQLException {
        List<TourModel> tours = new ArrayList<>();
        String query = "SELECT t.tour_id, t.package_id, t.tour_name, t.destination, t.start_date, t.end_date, "
                + "t.departure_location, t.prices, p.package_name "
                + "FROM tours t "
                + "LEFT JOIN packages p ON t.package_id = p.package_id "
                + "WHERE t.destination LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, "%" + destinationName + "%");
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

    public List<TourModel> searchToursByPackage(String package_name) throws SQLException {
        List<TourModel> tours = new ArrayList<>();
        String query = "SELECT t.tour_id, t.package_id, t.tour_name, t.destination, t.start_date, t.end_date, "
                + "t.departure_location, t.prices, p.package_name "
                + "FROM tours t "
                + "LEFT JOIN packages p ON t.package_id = p.package_id "
                + "WHERE p.package_name LIKE ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, "%" + package_name + "%");
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

    public List<String> getPackage() throws SQLException {
     List<String> list = new ArrayList<>();
     String query = "SELECT package_name from packages";
     try(Connection con = DatabaseConnection.getConnection(); 
         PreparedStatement pstm = con.prepareStatement(query)){
         
         ResultSet rs = pstm.executeQuery();
         while(rs.next()){
             list.add(rs.getString("package_name"));
         }  
     }
     return list;
    }
}
