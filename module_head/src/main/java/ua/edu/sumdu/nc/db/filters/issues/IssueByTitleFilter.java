package ua.edu.sumdu.nc.db.filters.issues;

import dao.DAO;
import entities.bt.Issue;
import ua.edu.sumdu.nc.db.dbparsers.DBParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class IssueByTitleFilter extends IssueFilter {
    private String title;
    private boolean isStrict;

    public IssueByTitleFilter(DBParser<Issue> parser, DAO dao) {
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
