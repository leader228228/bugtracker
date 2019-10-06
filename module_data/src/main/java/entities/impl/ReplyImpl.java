package entities.impl;

import dao.DAO;
import entities.bt.PersistenceEntity;
import entities.bt.Reply;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class ReplyImpl extends PersistenceEntity implements Reply {
    private long replyId;
    private String body;
    private long issueId;
    private long authorId;
    private Date created;

    public ReplyImpl(DAO DAO) {
        super(DAO);
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public long getReplyId() {
        return replyId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getIssueId() {
        return issueId;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    @Override
    public long getAuthorId() {
        return authorId;
    }

    @Override
    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    @Override
    public void save() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO BT_REPLIES ("
                     + "REPLY_ID, \"body\", ISSUE_ID, AUTHOR_ID) "
                     + "VALUES (?, ?, ?, ?)")) {
            preparedStatement.setLong(1, getReplyId());
            preparedStatement.setString(2, getBody());
            preparedStatement.setLong(3, getIssueId());
            preparedStatement.setLong(4, getAuthorId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement  = connection.prepareStatement("UPDATE BT_REPLIES SET " +
                     "\"body\" = ? " +
                     ", ISSUE_ID = ? " +
                     ", AUTHOR_ID = ? " +
                     "WHERE REPLY_ID = ?")){
            preparedStatement.setString(1, getBody());
            preparedStatement.setLong(2, getIssueId());
            preparedStatement.setLong(3, getAuthorId());
            preparedStatement.setLong(4, getReplyId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public void delete() throws SQLException {
        try (Connection connection = DAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_REPLIES WHERE REPLY_ID = ?");
            preparedStatement.setLong(1, getReplyId());
            preparedStatement.execute();
        }
    }

    @Override
    public String toString() {
        return "ReplyImpl{" +
                "replyId=" + replyId +
                ", body='" + body + '\'' +
                ", issueId=" + issueId +
                ", authorId=" + authorId +
                '}';
    }
}
