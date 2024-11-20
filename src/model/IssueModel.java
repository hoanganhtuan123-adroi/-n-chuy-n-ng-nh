package model;

import java.sql.Timestamp;

public class IssueModel {
    private int issueID;
    private String issueTitle;
    private String issueDescription;
    private Timestamp issueDate;
    private String issueStatus;
    private String issuePriority;

    public IssueModel() {}

    public IssueModel(int issueID, String issueTitle, String issueDescription, Timestamp issueDate, String issueStatus, String issuePriority) {
        this.issueID = issueID;
        this.issueTitle = issueTitle;
        this.issueDescription = issueDescription;
        this.issueDate = issueDate;
        this.issueStatus = issueStatus;
        this.issuePriority = issuePriority;
    }

    public IssueModel(String title, String description, String status, String priority) {
        this.issueTitle = title;
        this.issueDescription = description;
        this.issueStatus = status;
        this.issuePriority = priority;
    }


    public int getIssueID() {
        return issueID;
    }

    public void setIssueID(int issueID) {
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
