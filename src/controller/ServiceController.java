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

        //        SELECT
//           ROW_NUMBER() OVER AS  serviceID,
//            s.service_id,
//            s.service_name,
//            s.description,
//            p.package_name,
//            supp.supplier_name,
//            ps.included
//        FROM services s
//        LEFT JOIN package_services ps ON s.service_id = ps.service_id
//        LEFT JOIN packages p ON ps.package_id = p.package_id
//        LEFT JOIN supplier_services ss ON s.service_id = ss.service_id
//        LEFT JOIN suppliers supp ON ss.supplier_id = supp.supplier_id
//        ORDER BY s.service_id;

        String query = """
        SELECT 
             s.serviceID,
            s.service_id,
            s.service_name,
            s.description,
            p.package_name,
            supp.supplier_name,
            ps.included
        FROM new_table_service s
        LEFT JOIN package_services ps ON s.service_id = ps.service_id
        LEFT JOIN packages p ON ps.package_id = p.package_id
        LEFT JOIN supplier_services ss ON s.service_id = ss.service_id
        LEFT JOIN suppliers supp ON ss.supplier_id = supp.supplier_id
        ORDER BY s.serviceID;
      
    """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(rs.getString("serviceID"));
                int serviceId = rs.getInt("serviceID");
                String serviceName = rs.getString("service_name");
                String description = rs.getString("description");
                String packageName = rs.getString("package_name");
                String supplierName = rs.getString("supplier_name");
                boolean included = rs.getBoolean("included");

                serviceModels.add(new ServiceModel(serviceId, serviceName, description, packageName, supplierName, included));
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
                boolean included = rs.getBoolean("included");

                ServiceModel service = new ServiceModel(serviceId, serviceNameResult, description, packageName, supplierName, included);
                services.add(service);
            }
        }
        return services;
    }

    public ServiceModel getItemService(int serviceID){
        ServiceModel serviceModels = null;
        String query = """
        SELECT * FROM (SELECT 
            ROW_NUMBER() OVER() AS serviceID,
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
        ORDER BY s.service_id) AS subquery WHERE serviceID = ?;
    """;
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query);)
        {
            pstmt.setInt(1, serviceID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int serviceId = rs.getInt("serviceID");
                String serviceName = rs.getString("service_name");
                String description = rs.getString("description");
                String packageName = rs.getString("package_name");
                String supplierName = rs.getString("supplier_name");
                boolean included = rs.getBoolean("included");
                serviceModels = new ServiceModel(serviceId, serviceName, description, packageName, supplierName,included);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return serviceModels;

    }

    public boolean updateService(ServiceModel svModel) {
        String sql = "UPDATE services AS s " +
                "JOIN ( " +
                "    SELECT " +
                "        s.service_id, " +
                "        ? AS new_service_name, " + // Tham số 1
                "        ? AS new_description " + // Tham số 2
                "        ? AS new_service_supplier, " + // Tham số 3
                "        ? AS new_service_package " + // Tham số 4
                "    FROM services s " +
                "    LEFT JOIN package_services ps ON s.service_id = ps.service_id " +
                "    LEFT JOIN packages p ON ps.package_id = p.package_id " +
                "    LEFT JOIN supplier_services ss ON s.service_id = ss.service_id " +
                "    LEFT JOIN suppliers supp ON ss.supplier_id = supp.supplier_id " +
                "    WHERE s.service_id = ? " + // Tham số 5
                ") AS subquery ON s.service_id = subquery.service_id " +
                "SET " +
                "    s.service_name = subquery.new_service_name, " +
                "    s.description = subquery.new_description;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập các tham số
            stmt.setString(1, svModel.getServiceName()); // Tham số 1: new_service_name
            stmt.setString(2, svModel.getServiceDescription()); // Tham số 2: new_service_description
            stmt.setString(3, svModel.getServiceSupplier()); // Tham số 3: new_service_supplier
            stmt.setString(4, svModel.getServicePackageName());// Tham số 4: new_package_name
            stmt.setInt(5, svModel.getServiceId()); // Tham số 5: service_id

            // Thực thi câu lệnh
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    };
}
