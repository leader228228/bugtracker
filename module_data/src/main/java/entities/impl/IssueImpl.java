package entities.impl;

import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;

@Component
public class IssueImpl implements Issue {
    private long issueId;
    private long reporterId;
    private long assigneeId;
    private String title;
    private String body;
    private Timestamp created;
    private int statusId;
    private long projectId;

    @Autowired
    public IssueImpl() {
    }

    public IssueImpl(long issueId) {
        this.issueId = issueId;
    }

    public IssueImpl(long issueId, String body) {
        this.issueId = issueId;
        this.body = body;
    }

    public IssueImpl(long issueId, long reporterId, String body) {
        this.issueId = issueId;
        this.reporterId = reporterId;
        this.body = body;
    }

    public IssueImpl(long issueId, long reporterId, String body, int projectId) {
        this.issueId = issueId;
        this.reporterId = reporterId;
        this.body = body;
        this.projectId = projectId;
    }

    public IssueImpl(long issueId, String title, String body) {
        this.issueId = issueId;
        this.title = title;
        this.body = body;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public long getIssueId() {
        return issueId;
    }

    @Override
    public long getReporterId() {
        return reporterId;
    }

    @Override
    public void setReporterId(long reporterId) {
        this.reporterId = reporterId;
    }

    @Override
    public long getAssigneeId() {
        return assigneeId;
    }

    @Override
    public void setAssigneeId(long assigneeId) {
        this.assigneeId = assigneeId;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public Timestamp getCreated() {
        return created;
    }

    @Override
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public int getStatusId() {
        return statusId;
    }

    @Override
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @Override
    public long getProjectId() {
        return projectId;
    }

    @Override
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String getTitle() {
        return title;
    }

}
