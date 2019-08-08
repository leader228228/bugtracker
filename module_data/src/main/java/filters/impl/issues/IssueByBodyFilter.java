package filters.impl.issues;

import dao.DAO;
import entities.bt.Issue;
import dbparsers.DBParser;
import dbparsers.impl.issues.AllIssuesDBParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class IssueByBodyFilter extends IssueFilter {
    private String title;
    private boolean isStrict;

    public IssueByBodyFilter(DBParser<Issue> parser, DAO dao) {
        super(parser, dao);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStrict() {
        return isStrict;
    }

    public void setStrict(boolean strict) {
        isStrict = strict;
    }

    @Override
    public Collection<Issue> execute() throws SQLException {
        try (Connection connection = dao.getConnection()) {
            PreparedStatement preparedStatement;
            if (isStrict) {
                preparedStatement = connection.prepareStatement("SELECT * FROM BT_ISSUES WHERE BODY = ?");
            } else {
                preparedStatement = connection.prepareStatement("SELECT * FROM BT_ISSUES WHERE CONTAINS(BODY, ?)");
            }
            return new AllIssuesDBParser().parse(preparedStatement.executeQuery());
        }
    }
}
