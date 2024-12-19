package model;

import java.sql.Timestamp;

public class IssueModel {
    private String issueID;
    private String issueTitle;
    private String issueDescription;
    private Timestamp issueDate;
    private String issueStatus;
    private String issuePriority;
    private String employeeName;
    private String accountEmployee;

    public IssueModel(String issueID, String issueTitle, String issueDescription, Timestamp issueDate, String issueStatus, String issuePriority, String employeeName) {
        this.issueID = issueID;
        this.issueTitle = issueTitle;
        this.issueDescription = issueDescription;
        this.issueDate = issueDate;
        this.issueStatus = issueStatus;
        this.issuePriority = issuePriority;
        this.employeeName = employeeName;
    }

    public IssueModel(String issueID, String issueTitle, String issueDescription, Timestamp issueDate, String issueStatus, String issuePriority, String employeeName, String accountEmployee) {
        this.issueID = issueID;
        this.issueTitle = issueTitle;
        this.issueDescription = issueDescription;
        this.issueDate = issueDate;
        this.issueStatus = issueStatus;
        this.issuePriority = issuePriority;
        this.employeeName = employeeName;
        this.accountEmployee = accountEmployee;
    }

    public IssueModel(String issueID, String issueTitle, String issueDescription, Timestamp issueDate, String issueStatus, String issuePriority) {
        this.issueID = issueID;
        this.issueTitle = issueTitle;
        this.issueDescription = issueDescription;
        this.issueDate = issueDate;
        this.issueStatus = issueStatus;
        this.issuePriority = issuePriority;
    }

    public IssueModel(String title, String description, String status, String priority, String employeeName, String accountEmployee) {
        this.issueTitle = title;
        this.issueDescription = description;
        this.issueStatus = status;
        this.issuePriority = priority;
        this.employeeName = employeeName;
        this.accountEmployee = accountEmployee;
    }

    public String getAccountEmployee() {
        return accountEmployee;
    }

    public void setAccountEmployee(String accountEmployee) {
        this.accountEmployee = accountEmployee;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public Timestamp getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Timestamp issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getIssuePriority() {
        return issuePriority;
    }

    public void setIssuePriority(String issuePriority) {
        this.issuePriority = issuePriority;
    }
}
