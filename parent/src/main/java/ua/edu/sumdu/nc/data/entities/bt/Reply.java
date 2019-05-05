package ua.edu.sumdu.nc.data.entities.bt;

import ua.edu.sumdu.nc.data.dao.impl.DAOImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Reply extends Entity {
    long getReplyId();
    String getBody();
    void setBody(String body);
    long getIssueId();
    void setIssueId(long issueId);
    long getAuthorId();
    void setAuthorId(long authorId);

    @Override
    default void updateOrSave() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connection.prepareStatement("INSERT INTO BT_REPLIES ("
                        + "REPLY_ID, BODY, ISSUE_ID, AUTHOR_ID) "
                        + "VALUES (?, ?, ?, ?);");
                preparedStatement.setLong(1, getReplyId());
                preparedStatement.setString(2, getBody());
                preparedStatement.setLong(3, getIssueId());
                preparedStatement.setLong(4, getAuthorId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                preparedStatement = connection.prepareStatement("UPDATE BT_REPLIES SET " +
                        "BODY = ? " +
                        ", ISSUE_ID = ? " +
                        ", AUTHOR_ID = ? " +
                        "WHERE REPLY_ID = ?;");
                preparedStatement.setString(1, getBody());
                preparedStatement.setLong(2, getIssueId());
                preparedStatement.setLong(3, getAuthorId());
                preparedStatement.setLong(4, getReplyId());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    default void delete() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_REPLIES WHERE REPLY_ID = ?;");
            preparedStatement.setLong(1, getReplyId());
            preparedStatement.execute();
        }
    }
}
