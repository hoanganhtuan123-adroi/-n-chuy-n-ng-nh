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

    public static IssueModel getIssueById(int id) {
        IssueModel issue = null;
        String query = "SELECT * FROM issues WHERE issue_id = ?";
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
                issue = new IssueModel(issue_id,issue_title,issue_description, Timestamp.valueOf(issue_created), issue_status,issue_priority);
                return issue;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return issue;
    }

    public boolean updateIssue(IssueModel issues){
        boolean success = false;
        String query = "UPDATE issues SET issue_title=?, description=?, status=?, priority=?, created_at=? WHERE issue_id = ? ";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, issues.getIssueTitle());
            pstm.setString(2, issues.getIssueDescription());
            pstm.setString(3, issues.getIssueStatus());
            pstm.setString(4, issues.getIssuePriority());
            pstm.setTimestamp(5, issues.getIssueDate());
            pstm.setInt(6, issues.getIssueID());
            pstm.executeUpdate();
            success = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return success;
    }

    public boolean deleteIssue(int id) {
        boolean success = false;
        String query = "DELETE FROM issues WHERE issue_id = ?";
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setInt(1, id);
            pstm.executeUpdate();
            success = true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return success;
    }

    public boolean addNewIssue(IssueModel issue) {
        boolean success = false;
        String query = "INSERT INTO issues(issue_title, description, status, priority) VALUES(?,?,?,?)";
        try(Connection con = DatabaseConnection.getConnection();
        PreparedStatement pstm = con.prepareStatement(query)){
            pstm.setString(1, issue.getIssueTitle());
            pstm.setString(2, issue.getIssueDescription());
            pstm.setString(3, issue.getIssueStatus());
            pstm.setString(4, issue.getIssuePriority());
            pstm.executeUpdate();
            success = true;
        } catch (Exception e) {
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

}
