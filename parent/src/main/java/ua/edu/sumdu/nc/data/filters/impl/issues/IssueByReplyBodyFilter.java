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
public class IssueByReplyBodyFilter extends IssueFilter {
    private String replyBody;
    private boolean isStrict;


    public IssueByReplyBodyFilter(DAO dao, String replyBody, boolean isStrict) {
        super(dao);
        this.replyBody = replyBody;
        this.isStrict = isStrict;
    }

    @Override
    public Collection<Issue> execute() throws SQLException {
        try (Connection connection = dao.getConnection()) {
            PreparedStatement preparedStatement;
            if (isStrict) {
                preparedStatement = connection.prepareStatement(
                        "SELECT * " +
                        "FROM BT_ISSUES LEFT JOIN BT_REPLIES " +
                        "ON BT_ISSUES.ISSUE_ID = BT_REPLIES.ISSUE_ID " +
                        "WHERE BT_REPLIES.BODY = ?");
            } else {
                preparedStatement = connection.prepareStatement(
                        "SELECT * " +
                        "FROM BT_ISSUES LEFT JOIN BT_REPLIES " +
                        "ON BT_ISSUES.ISSUE_ID = BT_REPLIES.ISSUE_ID " +
                        "WHERE CONTAINS(BT_REPLIES.BODY, ?)");
            }
            preparedStatement.setString(1, replyBody);
            return new AllIssuesParser().parse(preparedStatement.executeQuery());
        }
    }
}
