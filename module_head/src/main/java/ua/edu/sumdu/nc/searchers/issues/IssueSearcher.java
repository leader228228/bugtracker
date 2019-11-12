package ua.edu.sumdu.nc.searchers.issues;

import dao.DAO;
import entities.bt.Issue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.edu.sumdu.nc.controllers.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@Component(value = "IssueSearcher")
@Scope(scopeName = "singleton")
public class IssueSearcher {
    private ApplicationContext appCtx;
    private DAO DAO;
    private Logger logger =  Logger.getRootLogger();
    public IssueSearcher(@Autowired DAO DAO, @Qualifier(value = "appConfig") ApplicationContext appCtx) {
        this.DAO = DAO;
        this.appCtx = appCtx;
    }

    public Issue getIssueByID(long issueId) {
        final String getIssueByIDQuery = "select * from bt_issues where issue_id = ?";
        try (
            Connection connection = DAO.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getIssueByIDQuery)
        ) {
            preparedStatement.setLong(1, issueId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return appCtx.getBean("Utils", Utils.class).readIssue(resultSet);
                    }
                    logger.error("Can not find issue (issue_id = " + issueId + ")");
                    throw new SQLException("Can not find issue (issue_id = " + issueId + ")");
              }
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }
}
