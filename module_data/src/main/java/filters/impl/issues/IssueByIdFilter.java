package filters.impl.issues;

import dao.DAO;
import entities.bt.Issue;
import dbparsers.DBParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class IssueByIdFilter extends IssueFilter {
    private long [] issueId;

    public IssueByIdFilter(DBParser<Issue> parser, DAO dao) {
        super(parser, dao);
    }

    public long[] getIssueId() {
        return issueId;
    }

    public void setIssueId(long[] issueId) {
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
            return parser.parse(preparedStatement.executeQuery());
        }
    }
}
