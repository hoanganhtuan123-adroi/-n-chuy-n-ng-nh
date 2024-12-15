package controller;

import database.DatabaseConnection;
import model.ServiceModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceController {
    public List<ServiceModel> getAllService(){
        List<ServiceModel> serviceModels = new ArrayList<>();
        String query = """
        SELECT 
             s.service_id, s.service_name, s.description, sup.supplier_name, p.package_name
        FROM services AS s
        LEFT JOIN suppliers AS sup ON s.supplier_id = sup.supplier_id
        LEFT JOIN packages AS p ON s.package_id = p.package_id 
    """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int serviceId = rs.getInt("service_id");
                String serviceName = rs.getString("service_name");
                String description = rs.getString("description");
                String packageName = rs.getString("package_name");
                String supplierName = rs.getString("supplier_name");

                serviceModels.add(new ServiceModel(serviceId, serviceName, description, packageName, supplierName));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return serviceModels;
    }

    public List<ServiceModel> searchServicesByName(String serviceName) throws SQLException {
        List<ServiceModel> services = new ArrayList<>();
        String query = """
        SELECT 
            s.service_id,
            s.service_name,
            s.description,
            p.package_name,
            supp.supplier_name,
            ps.included
        FROM services s
        LEFT JOIN package_services ps ON s.service_id = ps.service_id
        LEFT JOIN packages p ON ps.package_id = p.package_id
        LEFT JOIN supplier_services ss ON s.service_id = ss.service_id
        LEFT JOIN suppliers supp ON ss.supplier_id = supp.supplier_id
        WHERE s.service_name LIKE ?;
    """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, "%" + serviceName + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int serviceId = rs.getInt("service_id");
                String serviceNameResult = rs.getString("service_name");
                String description = rs.getString("description");
                String packageName = rs.getString("package_name");
                String supplierName = rs.getString("supplier_name");

                ServiceModel service = new ServiceModel(serviceId, serviceNameResult, description, packageName, supplierName);
                services.add(service);
            }
        }
        return services;
    }

    public ServiceModel getItemService(int serviceID){
        ServiceModel serviceModels = null;
        String query = """
        SELECT s.service_id, s.service_name, s.description, sup.supplier_name, p.package_name
        FROM services AS s 
        LEFT JOIN suppliers AS sup ON s.supplier_id = sup.supplier_id
        LEFT JOIN packages AS p ON s.package_id = p.package_id
        WHERE service_id = ?;
    """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);)
        {
            pstmt.setInt(1, serviceID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int serviceId = rs.getInt("service_id");
                String serviceName = rs.getString("service_name");
                String description = rs.getString("description");
                String packageName = rs.getString("package_name");
                String supplierName = rs.getString("supplier_name");
                serviceModels = new ServiceModel(serviceId, serviceName, description, packageName, supplierName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return serviceModels;

    }

    public int getPackageId(String packageName) throws SQLException {
        int packageID = 0;
        String query = "Select package_id from packages where package_name = ?";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, packageName);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                packageID = rs.getInt("package_id");
            }
        } return packageID;
    }

    public boolean updateService(ServiceModel svModel) {
        String sql ="update services as s\n" +
                "join packages as p on p.package_id = s.package_id\n" +
                "set s.service_name = ?, s.description =?, s.package_id = ?\n" +
                "where s.service_id = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập các tham số
            stmt.setString(1, svModel.getServiceName()); // Tham số 1: new_service_name
            stmt.setString(2, svModel.getServiceDescription()); // Tham số 2: new_service_description
            stmt.setInt(3, svModel.getPackageId());// Tham số 4: new_package_name
            stmt.setInt(4, svModel.getServiceId()); // Tham số 5: service_id

            // Thực thi câu lệnh
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    };

    public boolean deleteService(int serviceID) throws SQLException {
        String query = "DELETE FROM services WHERE service_id = ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, serviceID);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }
}
