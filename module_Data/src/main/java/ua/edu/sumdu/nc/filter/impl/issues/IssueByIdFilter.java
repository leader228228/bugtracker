package ua.edu.sumdu.nc.filter.impl.issues;

import ua.edu.sumdu.nc.dao.DAO;
import ua.edu.sumdu.nc.entities.bt.Issue;
import ua.edu.sumdu.nc.parsers.impl.issues.AllIssuesParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class IssueByIdFilter extends IssueFilter {
    private long [] issueId;

    public IssueByIdFilter(DAO dao, long...issueId) {
        super(dao);
        this.issueId = issueId;
    }

    @Override
    public Collection<Issue> execute() throws SQLException {
        try (Connection connection = dao.getConnection()) {
            Long[] longs = new Long[issueId.length];
            for (int i = 0; i < issueId.length; i++) {
                longs[i] = issueId[i];
            }
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM BT_ISSUES WHERE ISSUE_ID IN ?");
            preparedStatement.setArray(1, connection.createArrayOf("NUMBER", longs));
            return new AllIssuesParser().parse(preparedStatement.executeQuery());
        }
    }
}
