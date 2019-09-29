package ua.edu.sumdu.nc;

import dao.DAO;
import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@Scope(scopeName = "singleton")
public class Utils {
    private ApplicationContext applicationContext;
    private DAO DAO;
    public static final String QUERY_GET_NEXT_ID = "select getId() id from dual";

    public Utils(
        @Qualifier("appConfig") ApplicationContext applicationContext,
        @Qualifier(value = "DAO") DAO DAO
        ) {
        this.applicationContext = applicationContext;
        this.DAO = DAO;
    }

    public long getId() throws SQLException {
        try (Connection connection = DAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_NEXT_ID);
            return preparedStatement.executeQuery().getLong("id");
        }
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
    public Issue readIssue(ResultSet resultSet) throws SQLException {
        Issue issue = applicationContext.getBean("Issue", Issue.class);
        issue.setIssueId(resultSet.getLong("issue_id"));
        issue.setReporterId(resultSet.getLong("reporter_id"));
        issue.setProjectId(resultSet.getLong("project_id"));
        issue.setBody(resultSet.getString("body"));
        issue.setTitle(resultSet.getString("title"));
        issue.setAssigneeId(resultSet.getLong("assignee_id"));
        issue.setCreated(resultSet.getTimestamp("created"));
        issue.setStatusId(resultSet.getInt("status_id"));
        return issue;
    }
}
