package ua.edu.sumdu.nc.services.impl;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.entities.Reply;
import ua.edu.sumdu.nc.services.DBUtils;
import ua.edu.sumdu.nc.services.ReplyService;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@Service
public class ReplyServiceImpl implements ReplyService {

    private final DataSource dataSource;

    public ReplyServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static Reply readReply(ResultSet resultSet) throws SQLException {
        Reply reply = new Reply();
        reply.setAuthorID(resultSet.getLong("author_id"));
        reply.setBody(resultSet.getString("body"));
        reply.setIssueID(resultSet.getLong("issue_id"));
        reply.setReplyID(resultSet.getLong("reply_id"));
        reply.setCreated(resultSet.getDate("created"));
        return reply;
    }

    @Override
    public Reply createReply(long authorID, String body, long issueID) throws SQLException {
        Reply reply = new Reply();
        reply.setAuthorID(authorID);
        reply.setBody(body);
        reply.setCreated(new Date(System.currentTimeMillis()));
        reply.setIssueID(issueID);

        String insertReplyQuery =
                "begin insert into bt_replies(\"body\", issue_id, author_id, created) values(?, ?, ?, ?)" +
                        " returning reply_id into ?; end;";
        try(Connection connection = dataSource.getConnection();
            OracleCallableStatement callableStatement =
                    (OracleCallableStatement) connection.prepareCall(insertReplyQuery)) {
            callableStatement.registerOutParameter(5, OracleTypes.INTEGER);
            callableStatement.setString(1, reply.getBody());
            callableStatement.setLong(2, reply.getIssueID());
            callableStatement.setLong(3, reply.getAuthorID());
            callableStatement.setDate(4, reply.getCreated());
            callableStatement.execute();
            reply.setReplyID(callableStatement.getInt(5));
        }
        return reply;
    }

    @Override
    public boolean deleteReplies(Collection<Long> repliesIDs) throws SQLException {
        if (repliesIDs.size() == 0 || repliesIDs.contains(null)) {
            return false;
        }
        String _repliesIDs = repliesIDs.toString();
        String deleteRepliesQuery = "delete from bt_replies where reply_id in ("
            + _repliesIDs.substring(1, _repliesIDs.length() - 1) + ")";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteRepliesQuery)) {
            return preparedStatement.executeUpdate() != 0;
        }
    }

    @Override
    public Collection<Reply> getAll() throws SQLException {
        Collection<Reply> allReplies = new HashSet<>();
        String selectAllRepliesQuery = "select * from bt_replies";
        try (Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement(selectAllRepliesQuery).executeQuery()) {
            while (resultSet.next()) {
                allReplies.add(readReply(resultSet));
            }
        }
        return allReplies;
    }

    @Override
    public Collection<Reply> searchRepliesByAuthorsIDs(long[] authorsIDs) throws SQLException {
        if (authorsIDs.length == 0) {
            return getAll();
        }
        String _authorsIDs = Arrays.toString(authorsIDs);
        String selectRepliesByAuthorIDs =
            "select * from bt_replies where author_id in (" + _authorsIDs.substring(1, authorsIDs.length - 1) + ")";
        Collection<Reply> replies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.prepareStatement(selectRepliesByAuthorIDs).executeQuery()) {
            while (resultSet.next()) {
                replies.add(readReply(resultSet));
            }
        }
        return replies;
    }

    @Override
    public Collection<Reply> searchRepliesByText(String text) throws SQLException {
        String selectRepliesByTextQuery = "select * from bt_replies where body like ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectRepliesByTextQuery)) {
            preparedStatement.setString(1, DBUtils.getPatternContains(text));
            Collection<Reply> replies = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    replies.add(readReply(resultSet));
                }
            }
            return replies;
        }
    }

    @Override
    public Collection<Reply> searchRepliesByIssuesIDs(long[] issuesIDs) throws SQLException {
        if (issuesIDs.length == 0) {
            return getAll();
        }
        String _authorsIDs = Arrays.toString(issuesIDs);
        String selectRepliesByIssueIDs =
            "select * from bt_replies where issue_id in (" + _authorsIDs.substring(1, issuesIDs.length - 1) + ")";
        Collection<Reply> replies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.prepareStatement(selectRepliesByIssueIDs).executeQuery()) {
            while (resultSet.next()) {
                replies.add(readReply(resultSet));
            }
        }
        return replies;
    }
}
