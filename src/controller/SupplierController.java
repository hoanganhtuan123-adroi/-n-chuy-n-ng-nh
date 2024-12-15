package controller;

import database.DatabaseConnection;
import model.SupplierModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SupplierController {
    public List<SupplierModel> getListSupplier(){
        List<SupplierModel> listSupplier = new ArrayList<>();
        String query = "SELECT * FROM suppliers";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){

            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int id = rs.getInt("supplier_id");
                String supplierName = rs.getString("supplier_name");
                String contactName = rs.getString("contact_name");
                String contactEmail = rs.getString("contact_email");
                String contactPhone = rs.getString("contact_phone");
                String address = rs.getString("address");
                String serviceType = rs.getString("service_type");
                String description = rs.getString("description");
                listSupplier.add(new SupplierModel(id, supplierName, contactName, contactEmail, contactPhone, address, serviceType, description));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return listSupplier;
    }

    public boolean addSupplier(SupplierModel supplier){
        boolean isSuccess = false;
        String query = "INSERT INTO suppliers(supplier_name, contact_name, contact_email, contact_phone, address, service_type, description) VALUES (?,?,?,?,?,?,?)";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement psmt = con.prepareStatement(query)){
            psmt.setString(1, supplier.getSupplierName());
            psmt.setString(2, supplier.getContactName());
            psmt.setString(3, supplier.getContactEmail());
            psmt.setString(4, supplier.getContactPhone());
            psmt.setString(5, supplier.getAddress());
            psmt.setString(6, supplier.getServiceType());
            psmt.setString(7, supplier.getDescription());
            psmt.executeUpdate();
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return isSuccess;
    }

    public SupplierModel getSupplier(int id){
        SupplierModel supplier = null;
        String query = "SELECT * FROM suppliers WHERE supplier_id = ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                int supplierId = rs.getInt("supplier_id");
                String supplierName = rs.getString("supplier_name");
                String contactName = rs.getString("contact_name");
                String contactEmail = rs.getString("contact_email");
                String contactPhone = rs.getString("contact_phone");
                String address = rs.getString("address");
                String serviceType = rs.getString("service_type");
                String description = rs.getString("description");
                supplier = new SupplierModel(supplierId, supplierName, contactName, contactEmail, contactPhone, address, serviceType, description);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return supplier;
    }

    public boolean updateSupplier(SupplierModel supplier){
        System.out.println("Check ID >>>>" + supplier.getSupplierId());
        System.out.println("Check supplier name >>>>" + supplier.getSupplierName());
        boolean isSuccess = false;
        String query = "update suppliers set supplier_name =?, contact_name =?, contact_email =?, contact_phone =?, address =?, service_type=?, description=? where supplier_id = ?;";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstm = con.prepareStatement(query)) {
            pstm.setString(1, supplier.getSupplierName());
            pstm.setString(2, supplier.getContactName());
            pstm.setString(3, supplier.getContactEmail());
            pstm.setString(4, supplier.getContactPhone());
            pstm.setString(5, supplier.getAddress());
            pstm.setString(6,supplier.getServiceType());
            pstm.setString(7,supplier.getDescription());
            pstm.setInt(8,supplier.getSupplierId());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                isSuccess = true;
            } else {
                System.out.println("No rows were updated. Check if the supplier_id exists or the values are the same.");
                isSuccess = false;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Hoặc log lỗi tùy vào cách xử lý của bạn
            isSuccess = false;
        }
    return isSuccess;
    }

    public boolean deleteSupplier(int supplierID){
        String query = "DELETE FROM suppliers WHERE supplier_id=?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setInt(1,supplierID);
            int row = pstm.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<SupplierModel> searchSupplierByName(String searchText){
        List<SupplierModel> listSuppliers = new ArrayList<>();
        String query = "SELECT * FROM suppliers WHERE supplier_name LIKE ?";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, "%"+searchText+"%");
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int id = rs.getInt("supplier_id");
                String supplierName = rs.getString("supplier_name");
                String contactName = rs.getString("contact_name");
                String contactEmail = rs.getString("contact_email");
                String contactPhone = rs.getString("contact_phone");
                String address = rs.getString("address");
                String serviceType = rs.getString("service_type");
                String description = rs.getString("description");
                listSuppliers.add(new SupplierModel(id, supplierName, contactName, contactEmail, contactPhone, address, serviceType, description));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return listSuppliers;
    }

    public List<SupplierModel> searchSupplierByEmail(String searchText){
        List<SupplierModel> listSuppliers = new ArrayList<>();
        String query = "SELECT * FROM suppliers WHERE contact_email LIKE ?";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, "%"+searchText+"%");
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int id = rs.getInt("supplier_id");
                String supplierName = rs.getString("supplier_name");
                String contactName = rs.getString("contact_name");
                String contactEmail = rs.getString("contact_email");
                String contactPhone = rs.getString("contact_phone");
                String address = rs.getString("address");
                String serviceType = rs.getString("service_type");
                String description = rs.getString("description");
                listSuppliers.add(new SupplierModel(id, supplierName, contactName, contactEmail, contactPhone, address, serviceType, description));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return listSuppliers;
    }

    public List<SupplierModel> searchSupplierByService(String searchText){
        List<SupplierModel> listSuppliers = new ArrayList<>();
        String query = "SELECT * FROM suppliers WHERE service_type LIKE ?";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, "%"+searchText+"%");
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                int id = rs.getInt("supplier_id");
                String supplierName = rs.getString("supplier_name");
                String contactName = rs.getString("contact_name");
                String contactEmail = rs.getString("contact_email");
                String contactPhone = rs.getString("contact_phone");
                String address = rs.getString("address");
                String serviceType = rs.getString("service_type");
                String description = rs.getString("description");
                listSuppliers.add(new SupplierModel(id, supplierName, contactName, contactEmail, contactPhone, address, serviceType, description));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return listSuppliers;
    }
}
