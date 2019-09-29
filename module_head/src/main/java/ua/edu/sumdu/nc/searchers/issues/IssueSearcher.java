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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
@Component(value = "IssueSearcher")
@Scope(scopeName = "singleton")
public class IssueSearcher {
  private ApplicationContext appCtx;
  private DAO DAO;
  private Logger logger =  Logger.getRootLogger();
  public IssueSearcher(@Autowired DAO DAO, @Qualifier(value = "appConfig")ApplicationContext appCtx) {
    this.DAO = DAO;
    this.appCtx = appCtx;
  }

  public Issue getIssueByID(long issueID) {
    final String getIssueByIDQuery = "select * from bt_issues where issue_id = ?";
    try (
            Connection connection = DAO.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getIssueByIDQuery)
    ) {
      preparedStatement.setLong(1, issueID);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          Issue issue = appCtx.getBean("Issue", Issue.class);
          issue.setIssueId(resultSet.getLong("issue_id"));
          issue.setReporterId(resultSet.getLong("reporter_id"));
          issue.setProjectId(resultSet.getLong("project_id"));
          issue.setBody(resultSet.getString("body"));
          issue.setTitle(resultSet.getString("title"));
          issue.setAssigneeId(resultSet.getLong("assignee_id"));
          issue.setCreated(resultSet.getTimestamp("created"));
          issue.setStatusId(resultSet.getInt("status_id"));
          resultSet.close();
          return issue;
        }
        logger.error("Can not find issue (issue_id = " + issueID + ")");
        return null;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
