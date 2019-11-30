package ua.edu.sumdu.nc.validation.issues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.sumdu.nc.validation.BTRequest;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UpdateIssueRequest implements BTRequest {
@Min(value = 0)
    @JsonProperty(required = true)
    private Long issueID;
    @Min(value = 0)
    private Long assigneeID;
    private String body;
    private String title;
    @Min(value = 0)
    private Integer statusID;
    @Min(value = 0)
    private Integer projectID;
}
