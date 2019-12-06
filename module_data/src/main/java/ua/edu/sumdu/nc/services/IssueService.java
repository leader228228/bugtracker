package ua.edu.sumdu.nc.services;

import org.springframework.lang.Nullable;
import ua.edu.sumdu.nc.entities.Issue;

import java.sql.SQLException;
import java.util.Collection;

public interface IssueService {

    void updateIssue(long assigneeID, int statusID, long projectID, String body, String title, long issueID)
        throws SQLException;

    void deleteIssue(long issueID) throws SQLException;

    Issue createIssue(@Nullable Integer statusID, int projectID, String title, String body,
                      @Nullable Long assigneeID, long reporterID) throws SQLException;

    Collection<Issue> getAll() throws SQLException;

    Collection<Issue> getIssues(Collection<Long> issuesIDs) throws SQLException;

    Collection<Issue> getIssues(String text) throws SQLException;

    Collection<Issue> getIssuesByReporters(Collection<Long> reportersIDs) throws SQLException;

    Collection<Issue> getIssuesByAssignees(Collection<Long> reportersIDs) throws SQLException;
}
