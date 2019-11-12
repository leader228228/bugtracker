package ua.edu.sumdu.nc;

import entities.bt.Issue;
import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.controllers.EntityFactory;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class IssueService {

    private DataSource dataSource;

    public IssueService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void saveIssue(Issue issue) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO BT_ISSUES("
                + "ISSUE_ID, REPORTER_ID, ASSIGNEE_ID, CREATED, STATUS_ID, PROJECT_ID, \"body\", TITLE)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
        ) {
            preparedStatement.setLong(1, issue.getIssueId());
            preparedStatement.setLong(2, issue.getReporterId());
            preparedStatement.setLong(3, issue.getAssigneeId());
            preparedStatement.setDate(4, issue.getCreated());
            preparedStatement.setInt(5, issue.getStatusId());
            preparedStatement.setLong(6, issue.getProjectId());
            preparedStatement.setString(7, issue.getBody());
            preparedStatement.setString(8, issue.getTitle());
            preparedStatement.executeUpdate();
        }
    }

    public void updateIssue(Issue issue) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BT_ISSUES SET " +
                 "REPORTER_ID = ?" +
                 ", ASSIGNEE_ID = ?" +
                 ", CREATED = ?" +
                 ", STATUS_ID = ?" +
                 ", PROJECT_ID = ?" +
                 ", \"body\" = ?" +
                 ", TITLE = ?" +
                 " WHERE ISSUE_ID = ?")) {
            preparedStatement.setLong(1, issue.getReporterId());
            preparedStatement.setLong(2, issue.getAssigneeId());
            preparedStatement.setDate(3, issue.getCreated());
            preparedStatement.setInt(4, issue.getStatusId());
            preparedStatement.setLong(5, issue.getProjectId());
            preparedStatement.setString(6, issue.getBody());
            preparedStatement.setString(7, issue.getTitle());
            preparedStatement.setLong(8, issue.getIssueId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteIssue(Issue issue) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM BT_ISSUES WHERE ISSUE_ID = ?");
            preparedStatement.setLong(1, issue.getIssueId());
            preparedStatement.execute();
        }
    }

    /**
     * @param           createIssueRequest is expected to be valid. Validation is missed in this method
     * */
    public Issue createIssue(CreateIssueRequest createIssueRequest) throws SQLException {
        Issue issue = EntityFactory.get(Issue.class);
        issue.setStatusId(createIssueRequest.getStatusId() == null ? 0 : createIssueRequest.getStatusId());
        issue.setProjectId(createIssueRequest.getProjectId());
        issue.setTitle(createIssueRequest.getTitle());
        issue.setBody(createIssueRequest.getBody());
        issue.setAssigneeId(createIssueRequest.getAssigneeId() == null ? 0 : createIssueRequest.getAssigneeId());
        issue.setReporterId(createIssueRequest.getReporterId());
        issue.setCreated(new Date(System.currentTimeMillis()));
        saveIssue(issue);
        return issue;
    }
}
