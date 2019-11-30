package ua.edu.sumdu.nc.validation.issues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.edu.sumdu.nc.validation.BTRequest;

import javax.validation.constraints.Min;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DeleteIssueRequest implements BTRequest {
  @JsonProperty(required = true)
  @Min(value = 0)
  private long issueId;
}
