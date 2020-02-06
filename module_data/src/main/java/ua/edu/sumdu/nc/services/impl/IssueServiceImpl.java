package ua.edu.sumdu.nc.services.impl;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.entities.Issue;
import ua.edu.sumdu.nc.services.DBUtils;
import ua.edu.sumdu.nc.services.IssueService;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class IssueServiceImpl implements IssueService {

    private DataSource dataSource;

    public IssueServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     *  Reads an issue from {@code resultSet}
     *
     * @param           resultSet a result set of {@code bt_issues} containing all the table rows
     *
     * @return          an {@code Issue} bean
     *
     * !NOTE the method does not close the result set
     * !NOTE the method expects that you have invoked {@code ResultSet.next()} method before
     * */
    private static Issue readIssue(ResultSet resultSet) throws SQLException {
        Issue issue = new Issue();
        issue.setIssueID(resultSet.getLong("issue_id"));
        issue.setReporterID(resultSet.getLong("reporter_id"));
        issue.setProjectID(resultSet.getLong("project_id"));
        issue.setBody(resultSet.getString("body"));
        issue.setTitle(resultSet.getString("title"));
        issue.setAssigneeID(resultSet.getLong("assignee_id"));
        issue.setCreated(resultSet.getDate("created"));
        issue.setStatusID(resultSet.getInt("status_id"));
        return issue;
    }

    @Override
    public boolean updateIssue(long assigneeID, int statusID, long projectID, String body, String title, long issueID)
        throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BT_ISSUES SET " +
                 " ASSIGNEE_ID = ?" +
                 ", STATUS_ID = ?" +
                 ", PROJECT_ID = ?" +
                 ", \"body\" = ?" +
                 ", TITLE = ?" +
                 " WHERE ISSUE_ID = ?")) {
            preparedStatement.setLong(1, assigneeID);
            preparedStatement.setInt(2, statusID);
            preparedStatement.setLong(3, projectID);
            preparedStatement.setString(4, body);
            preparedStatement.setString(5, title);
            preparedStatement.setLong(6, issueID);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    @Override
    public boolean deleteIssue(long issueID) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM BT_ISSUES WHERE ISSUE_ID = ?");
            preparedStatement.setLong(1, issueID);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    @Override
    public Issue createIssue(@Nullable Integer statusID, int projectID, String title, String body,
                             @Nullable Long assigneeID, long reporterID) throws SQLException {
        Issue issue = new Issue();
        issue.setStatusID(statusID == null ? 0 : statusID);
        issue.setProjectID(projectID);
        issue.setTitle(title);
        issue.setBody(body);
        issue.setAssigneeID(assigneeID == null ? 0 : assigneeID);
        issue.setReporterID(reporterID);
        issue.setCreated(new Date(System.currentTimeMillis()));


        try (Connection connection = dataSource.getConnection();
             OracleCallableStatement callableStatement = (OracleCallableStatement) connection.prepareCall(
                     "begin insert into BT_ISSUES("
                             + "REPORTER_ID, ASSIGNEE_ID, CREATED, STATUS_ID, PROJECT_ID, \"body\", TITLE)"
                             + " values (?, ?, ?, ?, ?, ?, ?) returning issue_id into ?; end;")
        ) {
            callableStatement.registerOutParameter(8, OracleTypes.INTEGER);
            callableStatement.setLong(1, issue.getReporterID());
            callableStatement.setLong(2, issue.getAssigneeID());
            callableStatement.setDate(3, issue.getCreated());
            callableStatement.setInt(4, issue.getStatusID());
            callableStatement.setLong(5, issue.getProjectID());
            callableStatement.setString(6, issue.getBody());
            callableStatement.setString(7, issue.getTitle());
            callableStatement.execute();
            issue.setIssueID(callableStatement.getInt(8));
        }
        return issue;
    }

    @Override
    public Collection<Issue> getAll() throws SQLException {
        String getAllIssuesQuery = "select * from bt_issues";
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.prepareStatement(getAllIssuesQuery).executeQuery()) {
            Collection<Issue> allIssues = new ArrayList<>();
            while (resultSet.next()) {
                allIssues.add(readIssue(resultSet));
            }
            return allIssues;
        }
    }

    @Override
    public Collection<Issue> getIssues(Collection<Long> issuesIDs) throws SQLException {
        if (issuesIDs.size() == 0) {
            return getAll();
        } else {
            String _issuesIDs = issuesIDs.toString();
            String getIssuesQuery = "select * from bt_issues where issue_id in (" + _issuesIDs.substring(1, _issuesIDs.length() - 1) + ")";
            Collection<Issue> issues = new ArrayList<>();
            try (Connection connection = dataSource.getConnection();
                 ResultSet resultSet = connection.prepareStatement(getIssuesQuery).executeQuery()) {
                while (resultSet.next()) {
                    issues.add(readIssue(resultSet));
                }
                return issues;
            }
        }
    }

    @Override
    public Collection<Issue> getIssues(String text) throws SQLException {
        String getIssuesQuery = "select * from bt_issues i left join bt_replies r on i.issue_id = r.issue_id where i.body like ? or i.title like ? or r.\"body\" like ?";
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(getIssuesQuery);
            String pattern = DBUtils.getPatternContains(text);
            preparedStatement.setString(1, pattern);
            preparedStatement.setString(2, pattern);
            preparedStatement.setString(3, pattern);
            Collection<Issue> issues = new ArrayList<>();
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    issues.add(readIssue(resultSet));
                }
            }
            return issues;
        }
    }

    @Override
    public Collection<Issue> getIssuesByReporters(Collection<Long> reportersIDs) throws SQLException {
        if (reportersIDs.size() == 0) {
            return getAll();
        }
        String reporterIDs = reportersIDs.toString();
        String getIssuesQuery = "select * from bt_issues where reporter_id in (" + reporterIDs.substring(1, reporterIDs.length() - 1) + ")";
        Collection<Issue> issues = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement(getIssuesQuery).executeQuery()) {
            while (resultSet.next()) {
                issues.add(readIssue(resultSet));
            }
        }
        return issues;
    }

    @Override
    public Collection<Issue> getIssuesByAssignees(Collection<Long> assigneesIDs) throws SQLException {
        if (assigneesIDs.size() == 0) {
            return getAll();
        }
        String _assigneesIDs = assigneesIDs.toString();
        String getIssuesQuery =
                "select * from bt_issues where assignee_id in ("
                        + _assigneesIDs.substring(1, _assigneesIDs.length() - 1) + ")";
        Collection<Issue> issues = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement(getIssuesQuery).executeQuery()) {
            while (resultSet.next()) {
                issues.add(readIssue(resultSet));
            }
        }
        return issues;
    }
}
