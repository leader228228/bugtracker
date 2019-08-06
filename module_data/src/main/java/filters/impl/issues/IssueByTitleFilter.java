package filters.impl.issues;

import org.springframework.stereotype.Repository;
import dao.DAO;
import entities.bt.Issue;
import parsers.Parser;
import parsers.impl.issues.AllIssuesParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class IssueByTitleFilter extends IssueFilter {
    private String title;
    private boolean isStrict;

    public IssueByTitleFilter(Parser<Issue> parser, DAO dao) {
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
                preparedStatement = connection.prepareStatement(
                        "SELECT * FROM BT_ISSUES WHERE TITLE = ?;");
            } else {
                preparedStatement = connection.prepareStatement(
                        "SELECT * FROM BT_ISSUES WHERE CONTAINS(TITLE, ?);");
            }
            return parser.parse(preparedStatement.executeQuery());
        }
    }
}
