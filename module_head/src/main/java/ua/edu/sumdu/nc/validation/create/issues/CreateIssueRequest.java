package ua.edu.sumdu.nc.validation.create.issues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import ua.edu.sumdu.nc.validation.create.CreateRequest;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateIssueRequest implements CreateRequest {
    @Range(min = 1)
    @JsonProperty(required = true)
    private long reporterID;

    @Range(min = 0)
    @JsonProperty(defaultValue = "0")
    private Long assigneeID;

    @JsonProperty(required = true)
    @NotBlank
    @Length(max = 200, message = "{request.create.issue.error.toLongTitleMessage}")
    private String title;

    @NotBlank(message = "{request.create.issue.error.blankBodyMessage}")
    @Length(max = 4000, message = "{request.create.issue.error.toLongBodyMessage}")
    private String body;

    @Range(min = 1)
    private Integer statusID;

    @JsonProperty(required = true)
    @Range(min = 1)
    private int projectID;
}
