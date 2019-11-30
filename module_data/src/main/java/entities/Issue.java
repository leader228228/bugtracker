package entities;

import java.sql.Date;

public abstract class Issue implements Entity {

    private long issueID;
    private long reporterID;
    private long assigneeID;
    private String title;
    private String body;
    private Date created;
    private int statusID;
    private long projectID;

    public long getIssueID() {
        return issueID;
    }

    public void setIssueID(long issueID) {
        this.issueID = issueID;
    }

    public long getReporterID() {
        return reporterID;
    }

    public void setReporterID(long reporterID) {
        this.reporterID = reporterID;
    }

    public long getAssigneeID() {
        return assigneeID;
    }

    public void setAssigneeID(long assigneeID) {
        this.assigneeID = assigneeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public long getProjectID() {
        return projectID;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    @Override
    public String toString() {
        return "Issue{" +
            "issueID=" + issueID +
            ", reporterID=" + reporterID +
            ", assigneeID=" + assigneeID +
            ", title='" + title + '\'' +
            ", body='" + body + '\'' +
            ", created=" + created +
            ", statusID=" + statusID +
            ", projectID=" + projectID +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        if (issueID != issue.issueID) return false;
        if (reporterID != issue.reporterID) return false;
        if (assigneeID != issue.assigneeID) return false;
        if (statusID != issue.statusID) return false;
        if (projectID != issue.projectID) return false;
        if (!title.equals(issue.title)) return false;
        if (!body.equals(issue.body)) return false;
        return created.equals(issue.created);
    }

    @Override
    public int hashCode() {
        int result = (int) (issueID ^ (issueID >>> 32));
        result = 31 * result + (int) (reporterID ^ (reporterID >>> 32));
        result = 31 * result + (int) (assigneeID ^ (assigneeID >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + body.hashCode();
        result = 31 * result + created.hashCode();
        result = 31 * result + statusID;
        result = 31 * result + (int) (projectID ^ (projectID >>> 32));
        return result;
    }
}
