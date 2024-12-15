package controller;

import database.DatabaseConnection;
import model.IssueModel;
import model.ServiceModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssueController {
    public static List<IssueModel> getAllIssues() {
        List<IssueModel> issues = new ArrayList<>();
        String query = "SELECT * FROM issues";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query);) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("issue_id");
                String issue_title = rs.getString("issue_title");
                String issue_description = rs.getString("description");
                String issue_status = rs.getString("status");
                String issue_priority = rs.getString("priority");
                String issue_created = rs.getString("created_at");
                IssueModel model = new IssueModel(id, issue_title,issue_description, Timestamp.valueOf(issue_created), issue_status,issue_priority);
                issues.add(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return issues;
    }

    public static IssueModel getIssueById(int id) throws SQLException {
        IssueModel issue = null;
        String query = "select a.username, i.issue_id, i.issue_title, i.description, i.status, i.priority, i.created_at,  CONCAT(e.first_name, ' ', e.last_name) AS employeeName \n" +
                "from issues as i\n" +
                "left join employees as e on e.employee_id = i.employee_id " +
                "left join accounts as a on a.account_id = e.account_id where i.issue_id = ? ;";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                int issue_id = rs.getInt("issue_id");
                String issue_title = rs.getString("issue_title");
                String issue_description = rs.getString("description");
                String issue_status = rs.getString("status");
                String issue_priority = rs.getString("priority");
                String issue_created = rs.getString("created_at");
                String issue_employee = rs.getString("employeeName");
                String employee_account =rs.getString("username");
                issue = new IssueModel(issue_id,issue_title,issue_description, Timestamp.valueOf(issue_created), issue_status,issue_priority, issue_employee, employee_account);
            }
        }
        return issue;
    }

    public boolean updateIssue(IssueModel issues) throws SQLException {
        boolean success = false;
        String getIdEm = "SELECT e.employee_id FROM employees as e " +
                "left join accounts as a on a.account_id = e.account_id " +
                "where CONCAT(e.first_name, ' ', e.last_name) = ? and a.username = ?";
        try(Connection condb = DatabaseConnection.getConnection();
            PreparedStatement ps = condb.prepareStatement(getIdEm, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, issues.getEmployeeName());
            ps.setString(2, issues.getAccountEmployee());
            condb.setAutoCommit(false);
            ResultSet rsE = ps.executeQuery();
            int employee_id = 0;
            if (rsE.next()) {
                employee_id = rsE.getInt(1);
            }
            String query = "UPDATE issues as i LEFT JOIN employees as e on e.employee_id = i.employee_id " +
                    "SET i.issue_title=?, i.description=?, i.status=?, i.priority=?, i.created_at=?, i.employee_id =? WHERE i.issue_id = ? ";
            try(Connection con = DatabaseConnection.getConnection();
                PreparedStatement pstm = con.prepareStatement(query)){
                pstm.setString(1, issues.getIssueTitle());
                pstm.setString(2, issues.getIssueDescription());
                pstm.setString(3, issues.getIssueStatus());
                pstm.setString(4, issues.getIssuePriority());
                pstm.setTimestamp(5, issues.getIssueDate());
                pstm.setInt(6, employee_id);
                pstm.setInt(7, issues.getIssueID());
                pstm.executeUpdate();
                success = true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return success;
    }

    public boolean deleteIssue(int id) {
        boolean success = false;
        String deleteQuery = "DELETE FROM issues WHERE issue_id = ?";
        String getMaxIdQuery = "SELECT MAX(issue_id) FROM issues";
        String alterQuery = "ALTER TABLE issues AUTO_INCREMENT = ?";  // Sử dụng dấu ? cho PreparedStatement

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement deletePstm = con.prepareStatement(deleteQuery);
             Statement stmt = con.createStatement()) {

            // Xóa bản ghi
            deletePstm.setInt(1, id);
            deletePstm.executeUpdate();

            // Lấy giá trị MAX(issue_id)
            ResultSet rs = stmt.executeQuery(getMaxIdQuery);
            int maxId = 0;
            if (rs.next()) {
                maxId = rs.getInt(1);  // Lấy giá trị MAX(issue_id)
            }

            // Cập nhật lại giá trị AUTO_INCREMENT
            try (PreparedStatement alterPstm = con.prepareStatement(alterQuery)) {
                alterPstm.setInt(1, maxId + 1);  // Cập nhật AUTO_INCREMENT là MAX + 1
                alterPstm.executeUpdate();
            }

            success = true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return success;
    }


    public boolean addNewIssue(IssueModel issue) {
        boolean success = false;
        String getIdEm = "SELECT e.employee_id FROM employees as e " +
                "left join accounts as a on a.account_id = e.account_id " +
                "where CONCAT(e.first_name, ' ', e.last_name) = ? and a.username = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pt = connection.prepareStatement(getIdEm, Statement.RETURN_GENERATED_KEYS)){
            pt.setString(1, issue.getEmployeeName());
            pt.setString(2, issue.getAccountEmployee());
            connection.setAutoCommit(false);
            ResultSet rsE = pt.executeQuery();
            int employee_id = 0;
            if (rsE.next()) {
                employee_id = rsE.getInt(1);
            }

            String query = "INSERT INTO issues(issue_title, description, status, priority, employee_id) VALUES(?,?,?,?,?)";
            try(Connection con = DatabaseConnection.getConnection();
                PreparedStatement pstm = con.prepareStatement(query)){
                pstm.setString(1, issue.getIssueTitle());
                pstm.setString(2, issue.getIssueDescription());
                pstm.setString(3, issue.getIssueStatus());
                pstm.setString(4, issue.getIssuePriority());
                pstm.setInt(5, employee_id);
                pstm.executeUpdate();
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return success;
    }

    public List<IssueModel> searchIssueByTitle(String issueName) throws SQLException {
        List<IssueModel> issues = new ArrayList<>();
        String query = """
        select issue_id, issue_title, description, status, priority, created_at from issues where issue_title like ?;
    """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, "%" + issueName + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("issue_id");
                String issue_title = rs.getString("issue_title");
                String issue_description = rs.getString("description");
                String issue_status = rs.getString("status");
                String issue_priority = rs.getString("priority");
                String created_at = rs.getString("created_at");
                IssueModel issueModel = new IssueModel(id,issue_title,issue_description,Timestamp.valueOf(created_at),issue_status,issue_priority);
                issues.add(issueModel);
            }
        }
        return issues;
    }

    public List<String> getStatusList() throws SQLException {
        String query = "SELECT DISTINCT status FROM issues";
        List<String> status = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)){
            pstmt.executeQuery();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                status.add(rs.getString("status"));
            }
            return status;
        }
    }

    public List<String> getPriorityList() throws SQLException{
        List<String> priority = new ArrayList<>();
        String query = "SELECT DISTINCT priority FROM issues";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstmt = con.prepareStatement(query)){
            pstmt.executeQuery();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                priority.add(rs.getString("priority"));
            }
            return priority;
        }
    }

    public List<String> getEmployee() throws SQLException {
        List<String> employee = new ArrayList<>();
        String query = "SELECT a.username, CONCAT(e.first_name, ' ', e.last_name) AS employeeName " +
                "FROM employees AS e " +
                "LEFT JOIN accounts AS a ON a.account_id = e.account_id;";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                employee.add(rs.getString("employeeName") + " - " + rs.getString("username"));
            }
            return employee;
        }
    }

}
