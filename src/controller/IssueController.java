package controller;

import database.DatabaseConnection;
import model.IssueModel;
import model.ServiceModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IssueController {
    public static List<IssueModel> getAllIssues() {
        List<IssueModel> issues = new ArrayList<>();
        String query = "SELECT \n" +
                "    issue_id,\n" +
                "    issue_title,\n" +
                "    description,\n" +
                "    status,\n" +
                "    priority,\n" +
                "    created_at,\n" +
                "    IF(employee_id IS NULL, CONCAT('[DELETED] ', employee_name), employee_name) AS employee_display_name\n" +
                "FROM issues;";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query);) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String id = rs.getString("issue_id");
                String issue_title = rs.getString("issue_title");
                String issue_description = rs.getString("description");
                String issue_status = rs.getString("status");
                String issue_priority = rs.getString("priority");
                String employee_name = rs.getString("employee_display_name");
                String issue_created = rs.getString("created_at");
                IssueModel model = new IssueModel(id, issue_title,issue_description, Timestamp.valueOf(issue_created), issue_status,issue_priority, employee_name);
                issues.add(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return issues;
    }

    public static IssueModel getIssueById(String id) throws SQLException {
        IssueModel issue = null;
        String query = "select a.username, i.issue_id, i.issue_title, i.description, i.status, i.priority, i.created_at,  CONCAT(e.first_name, ' ', e.last_name) AS employeeName \n" +
                "from issues as i\n" +
                "left join employees as e on e.employee_id = i.employee_id " +
                "left join accounts as a on a.account_id = e.account where i.issue_id = ? ;";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, id);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()) {
                String issue_id = rs.getString("issue_id");
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
        String getIdEm = "SELECT e.employee_id,CONCAT(e.first_name, ' ', e.last_name) as name FROM employees as e " +
                "left join accounts as a on a.account_id = e.account " +
                "where CONCAT(e.first_name, ' ', e.last_name) = ? and a.username = ?";
        try(Connection condb = DatabaseConnection.getConnection();
            PreparedStatement ps = condb.prepareStatement(getIdEm, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, issues.getEmployeeName());
            ps.setString(2, issues.getAccountEmployee());
            condb.setAutoCommit(false);
            ResultSet rsE = ps.executeQuery();
            String employee_id = "";
            String employee_name = "";
            if (rsE.next()) {
                employee_id = rsE.getString(1);
                employee_name = rsE.getString(2);
            }
            String query = "UPDATE issues as i LEFT JOIN employees as e on e.employee_id = i.employee_id " +
                    "SET i.issue_title=?, i.description=?, i.status=?, i.priority=?, i.created_at=?, " +
                    "i.employee_id =?, i.employee_name = ? WHERE i.issue_id = ? ";
            try(Connection con = DatabaseConnection.getConnection();
                PreparedStatement pstm = con.prepareStatement(query)){
                pstm.setString(1, issues.getIssueTitle());
                pstm.setString(2, issues.getIssueDescription());
                pstm.setString(3, issues.getIssueStatus());
                pstm.setString(4, issues.getIssuePriority());
                pstm.setTimestamp(5, issues.getIssueDate());
                pstm.setString(6, employee_id);
                pstm.setString(7, employee_name);
                pstm.setString(8, issues.getIssueID());
                pstm.executeUpdate();
                success = true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return success;
    }

    public boolean deleteIssue(String id) {
        boolean success = false;
        String deleteQuery = "DELETE FROM issues WHERE issue_id = ?";

        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(deleteQuery)){
            pstm.setString(1, id);
            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                success = true;
            } else {
                throw new RuntimeException("Delete issue failed");
            }
        }
         catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return success;
    }

    /**
     * Adds a new issue to the database with the provided issue details.
     *
     */
    public boolean addNewIssue(IssueModel issue) {
        boolean success = false;
        String issueID = "ISSUE" + UUID.randomUUID().toString().substring(0, 5);
        String getIdEm = "SELECT e.employee_id,  CONCAT(e.first_name, ' ', e.last_name) FROM employees as e " +
                "left join accounts as a on a.account_id = e.account " +
                "where CONCAT(e.first_name, ' ', e.last_name) = ? and a.username = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pt = connection.prepareStatement(getIdEm, Statement.RETURN_GENERATED_KEYS)){
            pt.setString(1, issue.getEmployeeName());
            pt.setString(2, issue.getAccountEmployee());
            connection.setAutoCommit(false);
            ResultSet rsE = pt.executeQuery();
            String employee_id = "";
            String employee_name = "";
            if (rsE.next()) {
                employee_id = rsE.getString(1);
                employee_name = rsE.getString(2);
            }

            String query = "INSERT INTO issues(issue_id, issue_title, description, status, priority, employee_id, employee_name) VALUES(?,?,?,?,?,?, ?)";
            try(Connection con = DatabaseConnection.getConnection();
                PreparedStatement pstm = con.prepareStatement(query)){
                pstm.setString(1, issueID);
                pstm.setString(2, issue.getIssueTitle());
                pstm.setString(3, issue.getIssueDescription());
                pstm.setString(4, issue.getIssueStatus());
                pstm.setString(5, issue.getIssuePriority());
                pstm.setString(6, employee_id);
                pstm.setString(7, employee_name);
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

        String query = "SELECT \n" +
                "    issue_id,\n" +
                "    issue_title,\n" +
                "    description,\n" +
                "    status,\n" +
                "    priority,\n" +
                "    created_at,\n" +
                "    IF(employee_id IS NULL, CONCAT('[DELETED] ', employee_name), employee_name) AS employee_display_name\n" +
                "FROM issues where issue_title like ?;";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, "%" + issueName + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("issue_id");
                String issue_title = rs.getString("issue_title");
                String issue_description = rs.getString("description");
                String issue_status = rs.getString("status");
                String issue_priority = rs.getString("priority");
                String employee_name = rs.getString("employee_display_name");
                String issue_created = rs.getString("created_at");
                IssueModel model = new IssueModel(id, issue_title,issue_description, Timestamp.valueOf(issue_created), issue_status,issue_priority, employee_name);
                issues.add(model);
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
                "LEFT JOIN accounts AS a ON a.account_id = e.account;";
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
