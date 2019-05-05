package ua.edu.sumdu.nc.data.entities.impl;

import org.springframework.context.annotation.Bean;
import ua.edu.sumdu.nc.data.entities.bt.Issue;

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

}
