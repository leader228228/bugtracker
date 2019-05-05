package ua.edu.sumdu.nc.data.parsers.impl.issues;

import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.data.entities.bt.Issue;
import ua.edu.sumdu.nc.data.entities.impl.IssueImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

@Service
public class AllIssuesParser implements IssueParser {
    /**
     *  Parses the {@code ResultSet} extracting Issues from it
     *
     * @param           resultSet the result of executing the query {@code SELECT * FROM BT_ISSUES}
     * */
    @Override
    public Collection<Issue> parse(ResultSet resultSet) throws SQLException {
        Collection<Issue> result = new LinkedList<>();
        while (resultSet.next()) {
            IssueImpl issue = new IssueImpl(
                    resultSet.getLong("ISSUE_ID")
                    , resultSet.getString("TITLE")
                    , resultSet.getString("BODY"));
            issue.setReporterId(resultSet.getLong("REPORTER_ID"));
            issue.setAssigneeId(resultSet.getLong("ASSIGNEE_ID"));
            issue.setCreated(resultSet.getDate("CREATED"));
            issue.setStatusId(resultSet.getInt("STATUS_ID"));
            issue.setProjectId(resultSet.getLong("PROJECT_ID"));
            result.add(issue);
        }
        return result;
    }
}
