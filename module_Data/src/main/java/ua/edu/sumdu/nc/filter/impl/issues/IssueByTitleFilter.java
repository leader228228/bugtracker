package ua.edu.sumdu.nc.filter.impl.issues;

import ua.edu.sumdu.nc.dao.DAO;
import ua.edu.sumdu.nc.entities.bt.Issue;
import ua.edu.sumdu.nc.parsers.impl.issues.AllIssuesParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.regex.Pattern;

public class IssueByTitleFilter extends IssueFilter {
    private String title;
    private boolean isStrict;

    public IssueByTitleFilter(DAO dao, String title, boolean isStrict) {
        super(dao);
        this.title = title;
        this.isStrict = isStrict;
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
            return new AllIssuesParser().parse(preparedStatement.executeQuery());
        }
    }
}