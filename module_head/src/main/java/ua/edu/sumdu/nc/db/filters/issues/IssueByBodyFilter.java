package ua.edu.sumdu.nc.db.filters.issues;

import dao.DAO;
import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Qualifier;
import ua.edu.sumdu.nc.db.dbparsers.issues.AllIssuesDBParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class IssueByBodyFilter extends IssueFilter {
    private String title;
    private boolean isStrict;

    public IssueByBodyFilter( @Autowired AllIssuesDBParser parser, @Autowired DAO dao) {
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
            try {
                return parser.parse(preparedStatement.executeQuery());
            } catch (Exception e) {
                throw new RuntimeException("Unknown exception", e);
            }
        }
    }
}
