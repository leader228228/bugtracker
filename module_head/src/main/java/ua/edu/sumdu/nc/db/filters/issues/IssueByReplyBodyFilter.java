package ua.edu.sumdu.nc.db.filters.issues;

import dao.DAO;
import entities.bt.Issue;
import ua.edu.sumdu.nc.db.dbparsers.DBParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class IssueByReplyBodyFilter extends IssueFilter {
    private String replyBody;
    private boolean isStrict;

    public IssueByReplyBodyFilter(DBParser<Issue> parser, DAO dao) {
        super(parser, dao);
    }

    public String getReplyBody() {
        return replyBody;
    }

    public void setReplyBody(String replyBody) {
        this.replyBody = replyBody;
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
            return parser.parse(preparedStatement.executeQuery());
        }
    }
}
