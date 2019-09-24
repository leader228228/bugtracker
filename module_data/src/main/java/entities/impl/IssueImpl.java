package entities.impl;

import dao.DAO;
import entities.bt.Issue;
import entities.bt.PersistanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class IssueImpl extends PersistanceEntity implements Issue {
    private long issueId;
    private long reporterId;
    private long assigneeId;
    private String title;
    private String body;
    private Timestamp created;
    private int statusId;
    private long projectId;

    @Autowired
    public IssueImpl(DAO DAO) {
        super(DAO);
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public long getIssueId() {
        return issueId;
    }

    @Override
    public long getReporterId() {
        return reporterId;
    }

    @Override
    public void setReporterId(long reporterId) {
        this.reporterId = reporterId;
    }

    @Override
    public long getAssigneeId() {
        return assigneeId;
    }

    @Override
    public void setAssigneeId(long assigneeId) {
        this.assigneeId = assigneeId;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public Timestamp getCreated() {
        return created;
    }

    @Override
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public int getStatusId() {
        return statusId;
    }

    @Override
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @Override
    public long getProjectId() {
        return projectId;
    }

    @Override
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void update() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BT_ISSUES SET " +
                     "REPORTER_ID = ?" +
                     ", ASSIGNEE_ID = ?" +
                     ", CREATED = ?" +
                     ", STATUS_ID = ?" +
                     ", PROJECT_ID = ?" +
                     ", BODY = ?" +
                     ", TITLE = ?" +
                     " WHERE ISSUE_ID = ?")) {
            preparedStatement.setLong(1, getReporterId());
            preparedStatement.setLong(2, getAssigneeId());
            preparedStatement.setTimestamp(3, getCreated());
            preparedStatement.setInt(4, getStatusId());
            preparedStatement.setLong(5, getProjectId());
            preparedStatement.setString(6, getBody());
            preparedStatement.setString(7, getTitle());
            preparedStatement.setLong(8, getIssueId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void save() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO BT_ISSUES("
                             + "ISSUE_ID, REPORTER_ID, ASSIGNEE_ID, CREATED, STATUS_ID, PROJECT_ID, \"body\", TITLE)"
                             + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setLong(1, getIssueId());
            preparedStatement.setLong(2, getReporterId());
            preparedStatement.setLong(3, getAssigneeId());
            preparedStatement.setTimestamp(4, getCreated());
            preparedStatement.setInt(5, getStatusId());
            preparedStatement.setLong(6, getProjectId());
            preparedStatement.setString(7, getBody());
            preparedStatement.setString(8, getTitle());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete() throws SQLException {
        try (Connection connection = DAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_ISSUES WHERE ISSUE_ID = ?;");
            preparedStatement.setLong(1, getIssueId());
            preparedStatement.execute();
        }
    }

    @Override
    public String toString() {
        return "IssueImpl{" +
                "issueId=" + issueId +
                ", reporterId=" + reporterId +
                ", assigneeId=" + assigneeId +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", created=" + created +
                ", statusId=" + statusId +
                ", projectId=" + projectId +
                '}';
    }
}
