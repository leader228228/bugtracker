package ua.edu.sumdu.nc;

import entities.bt.Issue;
import entities.bt.Project;
import entities.bt.Reply;
import entities.bt.User;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.searchers.issues.IssueSearcher;
import ua.edu.sumdu.nc.searchers.projects.ProjectSearcher;
import ua.edu.sumdu.nc.searchers.replies.ReplySearcher;
import ua.edu.sumdu.nc.searchers.users.UserSearcher;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@Scope(scopeName = "singleton")
public class Utils {
    private ApplicationContext appCtx;

    public Utils(@Qualifier("appConfig") ApplicationContext appCtx) {
        this.appCtx = appCtx;
    }

    /**
     *  Reads an issue from {@code resultSet}
     *
     * @param           resultSet a result set of {@code bt_issues} containing all the table rows
     *
     * @return          an {@code Issue} bean
     *
     * !NOTE the method does not close the result set
     * !NOTE the method expects that you have invoked {@code ResultSet.next()} method before
     * */
    public Issue readIssue(ResultSet resultSet) throws SQLException {
        Issue issue = appCtx.getBean("Issue", Issue.class);
        issue.setIssueId(resultSet.getLong("issue_id"));
        issue.setReporterId(resultSet.getLong("reporter_id"));
        issue.setProjectId(resultSet.getLong("project_id"));
        issue.setBody(resultSet.getString("body"));
        issue.setTitle(resultSet.getString("title"));
        issue.setAssigneeId(resultSet.getLong("assignee_id"));
        issue.setCreated(resultSet.getDate("created"));
        issue.setStatusId(resultSet.getInt("status_id"));
        return issue;
    }

    public User readUser(ResultSet resultSet) throws SQLException {
        User user = appCtx.getBean("User",User.class);
        user.setUserId(resultSet.getLong("user_id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        return user;
    }

    public Reply readReply(ResultSet resultSet) throws SQLException {
        Reply reply = appCtx.getBean("Reply",Reply.class);
        reply.setAuthorId(resultSet.getLong("author_id"));
        reply.setBody(resultSet.getString("body"));
        reply.setIssueId(resultSet.getLong("issue_id"));
        reply.setReplyId(resultSet.getLong("reply_id"));
        reply.setCreated(resultSet.getDate("created"));
        return reply;
    }

    @Lookup(value = "UserSearcher")
    public UserSearcher getUserSearcher() {
        return null;
    }

    @Lookup(value = "Issue")
    public Issue getIssue() {
        return null;
    }

    @Lookup(value = "User")
    public User getUser() {
        return null;
    }

    @Lookup(value = "Project")
    public Project getProject() {
        return null;
    }

    @Lookup(value = "Reply")
    public Reply getReply() {
        return null;
    }

    @Lookup(value = "IssueSearcher")
    public IssueSearcher getIssueSearcher() {
        return null;
    }

    @Lookup(value = "ReplySearcher")
    public ReplySearcher getReplySearcher() {
        return null;
    }

    @Lookup(value = "ProjectSearcher")
    public ProjectSearcher getProjectSearcher() {
        return null;
    }
}
