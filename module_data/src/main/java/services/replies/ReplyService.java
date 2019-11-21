package services.replies;

import entities.EntityFactory;
import entities.bt.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.DBUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@Service
public class ReplyService {
    @Autowired
    private DataSource dataSource;

    private void saveReply(Reply reply) throws SQLException {
        String insertReplyQuery = "insert into bt_replies(\"body\", issue_id, author_id, created) values(?, ?, ?, ?)";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertReplyQuery)) {
            preparedStatement.setString(1, reply.getBody());
            preparedStatement.setLong(2, reply.getIssueID());
            preparedStatement.setLong(3, reply.getAuthorID());
            preparedStatement.setDate(4, reply.getCreated());
            preparedStatement.executeUpdate();
        }
    }

    public Reply createReply(long authorID, String body, long issueID) throws SQLException {
        Reply reply = EntityFactory.get(Reply.class);
        reply.setAuthorID(authorID);
        reply.setBody(body);
        reply.setCreated(new Date(System.currentTimeMillis()));
        reply.setIssueID(issueID);
        saveReply(reply);
        return reply;
    }

    public void deleteReplies(Collection<Long> repliesIDs) throws SQLException {
        if (repliesIDs.size() == 0 || repliesIDs.contains(null)) {
            return;
        }
        //Collection<Long> repliesIDs = replies.stream().map(Reply::getReplyId).collect(Collectors.toSet());
        String _repliesIDs = repliesIDs.toString();
        String deleteRepliesQuery = "delete from bt_replies where reply_id in ("
            + _repliesIDs.substring(1, _repliesIDs.length() - 1) + ")";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteRepliesQuery)) {
            preparedStatement.executeUpdate();
        }
    }

    public Collection<Reply> getAll() throws SQLException {
        Collection<Reply> allReplies = new HashSet<>();
        String selectAllRepliesQuery = "select * from bt_replies";
        try (Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement(selectAllRepliesQuery).executeQuery()) {
            while (resultSet.next()) {
                allReplies.add(DBUtils.readReply(resultSet));
            }
        }
        return allReplies;
    }

    public Collection<Reply> searchRepliesByAuthorsIDs(long [] authorsIDs) throws SQLException {
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
                replies.add(DBUtils.readReply(resultSet));
            }
        }
        return replies;
    }

    public Collection<Reply> searchRepliesByText(String text) throws SQLException {
        String selectRepliesByTextQuery = "select * from bt_replies where body like ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectRepliesByTextQuery)) {
            preparedStatement.setString(1, DBUtils.getPatternContains(text));
            Collection<Reply> replies = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    replies.add(DBUtils.readReply(resultSet));
                }
            }
            return replies;
        }
    }

    public Collection<Reply> searchRepliesByIssuesIDs(long [] issuesIDs) throws SQLException {
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
                replies.add(DBUtils.readReply(resultSet));
            }
        }
        return replies;
    }
}
