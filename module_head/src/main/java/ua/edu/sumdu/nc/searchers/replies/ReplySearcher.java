package ua.edu.sumdu.nc.searchers.replies;

import dao.DAO;
import entities.bt.Reply;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
@Component(value = "ReplySearcher")
@Scope(scopeName = "singleton")
public class ReplySearcher {
    private ApplicationContext appCtx;
    private DAO DAO;
    private Logger logger =  Logger.getRootLogger();

    public ReplySearcher(@Autowired DAO DAO, @Qualifier(value = "appConfig") ApplicationContext appCtx) {
        this.DAO = DAO;
        this.appCtx = appCtx;
    }

    public Reply getReplyByID(long replyID) {
        final String getReplyByIDQuery = "select * from bt_replies where reply_id = ?";
        try (
                Connection connection = DAO.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getReplyByIDQuery)
        ) {
            preparedStatement.setLong(1, replyID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Reply reply = appCtx.getBean("Reply", Reply.class);
                    reply.setReplyId(resultSet.getLong("reply_id"));
                    reply.setIssueId(resultSet.getLong("issue_id"));
                    reply.setBody(resultSet.getString("body"));
                    reply.setAuthorId(resultSet.getLong("author_id"));
                    return reply;
                }
                logger.error("Can not find reply (reply_id = " + replyID + ")");
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
