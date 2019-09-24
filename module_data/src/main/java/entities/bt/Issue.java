package entities.bt;

import java.sql.Timestamp;

public interface Issue extends Entity {
    long getIssueId();
    long getReporterId();
    void setReporterId(long reporterId);
    long getAssigneeId();
    void setAssigneeId(long assigneeId);
    String getBody();
    String getTitle();
    void setBody(String body);
    Timestamp getCreated();
    void setCreated(Timestamp created);
    int getStatusId();
    void setStatusId(int statusId);
    long getProjectId();
    void setProjectId(long projectId);
    void setIssueId(long issueId);
    void setTitle(String title);
}
