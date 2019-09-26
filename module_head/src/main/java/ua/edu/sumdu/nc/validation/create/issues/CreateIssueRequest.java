package ua.edu.sumdu.nc.validation.create.issues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import ua.edu.sumdu.nc.validation.create.CreateRequest;

import javax.validation.constraints.Max;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateIssueRequest implements CreateRequest {
    @Range(min = 1)
    @JsonProperty(required = true)
    private long reporterId;

    @Range(min = 0)
    @JsonProperty(defaultValue = "0")
    private long assigneeId;

    @JsonProperty(required = true)
    @NotBlank
    @Length(max = 200, message = "{request.create.issue.error.toLongTitleMessage}")
    private String title;

    @NotBlank(message = "{request.create.issue.error.blankBodyMessage}")
    @Length(max = 4000, message = "{request.create.issue.error.toLongBodyMessage}")
    private String body;

    @JsonProperty(defaultValue = "0")
    @Range(min = 1)
    private int statusId;

    @JsonProperty(required = true)
    @Range(min = 1)
    private long projectId;

    public long getReporterId() {
        return reporterId;
    }

    public void setReporterId(long reporterId) {
        this.reporterId = reporterId;
    }

    public long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(long assigneeId) {
        this.assigneeId = assigneeId;
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

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "CreateIssueRequest{" +
                "reporterId=" + reporterId +
                ", assigneeId=" + assigneeId +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", statusId=" + statusId +
                ", projectId=" + projectId +
                '}';
    }
}
