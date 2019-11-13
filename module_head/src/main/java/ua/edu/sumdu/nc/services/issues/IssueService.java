package ua.edu.sumdu.nc.services.issues;

import entities.bt.Issue;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.controllers.EntityFactory;
import ua.edu.sumdu.nc.controllers.Utils;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;
import ua.edu.sumdu.nc.validation.update.issues.UpdateIssueRequest;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Service
public class IssueService {

    private DataSource dataSource;

    private final Logger logger = Logger.getRootLogger();

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
            preparedStatement.setLong(1, issue.getIssueID());
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

    public void updateIssue(UpdateIssueRequest updateIssueRequest) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BT_ISSUES SET " +
                 " ASSIGNEE_ID = ?" +
                 ", STATUS_ID = ?" +
                 ", PROJECT_ID = ?" +
                 ", \"body\" = ?" +
                 ", TITLE = ?" +
                 " WHERE ISSUE_ID = ?")) {
            preparedStatement.setLong(1, updateIssueRequest.getAssigneeID());
            preparedStatement.setInt(2, updateIssueRequest.getStatusID());
            preparedStatement.setLong(3, updateIssueRequest.getProjectID());
            preparedStatement.setString(4, updateIssueRequest.getBody());
            preparedStatement.setString(5, updateIssueRequest.getTitle());
            preparedStatement.setLong(6, updateIssueRequest.getIssueID());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteIssue(Issue issue) throws SQLException {
        deleteIssue(issue.getIssueID());
    }

    public void deleteIssue(long issueID) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM BT_ISSUES WHERE ISSUE_ID = ?");
            preparedStatement.setLong(1, issueID);
            preparedStatement.executeUpdate();
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

    public Collection<Issue> getAll() throws SQLException {
        String getAllIssuesQuery = "select * from bt_issues";
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.prepareStatement(getAllIssuesQuery).executeQuery()) {
            Collection<Issue> allIssues = new ArrayList<>();
            while (resultSet.next()) {
                allIssues.add(Utils.readIssue(resultSet));
            }
            return allIssues;
        }
    }

    public Collection<Issue> getIssues(long [] issuesIDs) throws SQLException {
        if (issuesIDs.length == 0) {
            return getAll();
        } else {
            String _issuesIDs = Arrays.toString(issuesIDs);
            String getIssues = "select * from bt_issues where issue_id in (" + _issuesIDs.substring(1, _issuesIDs.length() - 1) + ")";
            Collection<Issue> issues = new ArrayList<>();
            try (Connection connection = dataSource.getConnection();
                 ResultSet resultSet = connection.prepareStatement(getIssues).executeQuery()) {
                while (resultSet.next()) {
                    issues.add(Utils.readIssue(resultSet));
                }
                return issues;
            }
        }
    }

    public Collection<Issue> getIssues(String text) throws SQLException {
        String getIssues = "select * from bt_issues i left join bt_replies r on i.issue_id = r.issue_id where i.body like ? or i.title like ? or r.body like ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(getIssues);
            String pattern = Utils.getPatternContains(text);
            preparedStatement.setString(1, pattern);
            preparedStatement.setString(2, pattern);
            preparedStatement.setString(3, pattern);
            Collection<Issue> issues = new ArrayList<>();
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    issues.add(Utils.readIssue(resultSet));
                }
            }
            return issues;
        }
    }

    public Collection<Issue> getIssuesByReporters(long [] reportersIDs) throws SQLException {
        if (reportersIDs.length == 0) {
            return getAll();
        }
        String reporterIDs = Arrays.toString(reportersIDs);
        String getIssues = "select * from bt_issues where reporter_id in (" + reporterIDs.substring(1, reporterIDs.length() - 1) + ")";
        Collection<Issue> issues = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement(getIssues).executeQuery()) {
            while (resultSet.next()) {
                issues.add(Utils.readIssue(resultSet));
            }
        }
        return issues;
    }

    public Collection<Issue> getIssuesByAssignees(long [] reportersIDs) throws SQLException {
        if (reportersIDs.length == 0) {
            return getAll();
        }
        String _reportersIDs = Arrays.toString(reportersIDs);
        String getIssues = "select * from bt_issues where assignee_id in (" + _reportersIDs.substring(1, _reportersIDs.length() - 1) + ")";
        Collection<Issue> issues = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement(getIssues).executeQuery()) {
            while (resultSet.next()) {
                issues.add(Utils.readIssue(resultSet));
            }
        }
        return issues;
    }
}
