package services;

import entities.EntityFactory;
import entities.Issue;
import entities.Project;
import entities.Reply;
import entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    private static final char SQL_ESCAPE_CHAR = '&';

    public static String getPatternContains(String string) {
        return '%' + escapeRegexChars(string) + '%';
    }

    private static String escapeRegexChars(String string) {
        return string
            .replaceAll("%",SQL_ESCAPE_CHAR + "%")
            .replaceAll(String.valueOf(SQL_ESCAPE_CHAR), "" + SQL_ESCAPE_CHAR + SQL_ESCAPE_CHAR)
            .replaceAll("_", SQL_ESCAPE_CHAR + "_");
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
    public static Issue readIssue(ResultSet resultSet) throws SQLException {
        Issue issue = EntityFactory.get(Issue.class);
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

    public static User readUser(ResultSet resultSet) throws SQLException {
        User user = EntityFactory.get(User.class);
        user.setUserID(resultSet.getLong("user_id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        return user;
    }

    public static Project readProject(ResultSet resultSet) throws SQLException {
        Project project = EntityFactory.get(Project.class);
        project.setName(resultSet.getString("name"));
        project.setProjectID(resultSet.getInt("project_id"));
        return project;
    }

    public static Reply readReply(ResultSet resultSet) throws SQLException {
        Reply reply = EntityFactory.get(Reply.class);
        reply.setAuthorID(resultSet.getLong("author_id"));
        reply.setBody(resultSet.getString("body"));
        reply.setIssueID(resultSet.getLong("issue_id"));
        reply.setReplyID(resultSet.getLong("reply_id"));
        reply.setCreated(resultSet.getDate("created"));
        return reply;
    }
}
