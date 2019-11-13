package entities.impl;

import entities.bt.Issue;

import java.sql.Date;

public class IssueImpl implements Issue {

    private long issueId;
    private long reporterId;
    private long assigneeId;
    private String title;
    private String body;
    private Date created;
    private int statusId;
    private long projectId;

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public long getIssueID() {
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
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
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

    @Override
    public String toString() {
        return "IssueImpl{" +
                "issueId=" + issueId +
                ", reporterId=" + reporterId +
                ", assigneeId=" + assigneeId +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", created=" + created +
                ", statusId=" + statusId +
                ", projectId=" + projectId +
                '}';
    }
}
