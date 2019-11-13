package entities.bt;

import java.sql.Date;

public interface Issue extends Entity {
    long getIssueID();
    long getReporterId();
    void setReporterId(long reporterId);
    long getAssigneeId();
    void setAssigneeId(long assigneeId);
    String getBody();
    String getTitle();
    void setBody(String body);
    Date getCreated();
    void setCreated(Date created);
    int getStatusId();
    void setStatusId(int statusId);
    long getProjectId();
    void setProjectId(long projectId);
    void setIssueId(long issueId);
    void setTitle(String title);
}
