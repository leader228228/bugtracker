package ua.edu.sumdu.nc.data.filters.impl.issues;

import org.springframework.stereotype.Repository;
import ua.edu.sumdu.nc.data.dao.DAO;
import ua.edu.sumdu.nc.data.entities.bt.Issue;
import ua.edu.sumdu.nc.data.parsers.impl.issues.AllIssuesParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class IssueByBodyFilter extends IssueFilter {
    private String title;
    private boolean isStrict;

    public IssueByBodyFilter(DAO dao, String title, boolean isStrict) {
        super(dao);
        this.title = title;
        this.isStrict = isStrict;
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
            return new AllIssuesParser().parse(preparedStatement.executeQuery());
        }
    }
}
