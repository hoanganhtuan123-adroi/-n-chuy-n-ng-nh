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

    public List<ServiceModel> searchServicesByName(String service_Name) throws SQLException {
        List<ServiceModel> services = new ArrayList<>();
        String query = """
          SELECT  s.service_id, s.service_name, s.description, sup.supplier_name, p.package_name
               FROM services AS s
               LEFT JOIN suppliers AS sup ON s.supplier_id = sup.supplier_id
               LEFT JOIN packages AS p ON s.package_id = p.package_id\s
               where s.service_name Like ?;
          """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, "%" + service_Name + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int serviceId = rs.getInt("service_id");
                String serviceName = rs.getString("service_name");
                String description = rs.getString("description");
                String packageName = rs.getString("package_name");
                String supplierName = rs.getString("supplier_name");

                ServiceModel service = new ServiceModel(serviceId, serviceName, description, packageName, supplierName);
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

    public List<String> getSupplier() throws SQLException {
        List<String> listSupplier = new ArrayList<>();
        String query = "select distinct(supplier_name) from suppliers;";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                listSupplier.add(rs.getString("supplier_name"));
            }
        }
        return listSupplier;
    }

    public int getSupplierID(String supplierName) throws SQLException {
        int supplierID = 0;
        String query = "Select supplier_id from suppliers where supplier_name = ?;";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, supplierName);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    supplierID = rs.getInt("supplier_id");
                }
            }
        }
        return supplierID;
    }

    public boolean handleAddNewService(ServiceModel services){
        String query = "INSERT INTO services(service_name, description, supplier_id, package_id) VALUES(?,?,?,?); ";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, services.getServiceName());
            pstm.setString(2, services.getServiceDescription());
            pstm.setInt(3, services.getSupplierID());
            pstm.setInt(4, services.getPackageId());
            int rowsUpdated = pstm.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
