package ua.edu.sumdu.nc.filter.impl.issues;

import ua.edu.sumdu.nc.dao.DAO;
import ua.edu.sumdu.nc.entities.bt.Issue;
import ua.edu.sumdu.nc.parsers.impl.issues.AllIssuesParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.regex.Pattern;

public class IssueByBodyFilter extends IssueFilter {
    private String title;

    public IssueByBodyFilter(DAO dao, String title, boolean isStrict) {
        super(dao);
        this.title = isStrict ? '^' + Pattern.quote(title) + '$' : '%' + Pattern.quote(title) + '%';
    }

    @Override
    public Collection<Issue> execute() throws SQLException {
        try (Connection connection = dao.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM BT_ISSUES WHERE BODY LIKE ?");
            preparedStatement.setString(1, title);
            return new AllIssuesParser().parse(preparedStatement.executeQuery());
        }
    }
}