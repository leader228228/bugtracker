package ua.edu.sumdu.nc.validation.delete.issues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.edu.sumdu.nc.validation.delete.DeleteRequest;

import javax.validation.constraints.Min;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DeleteIssueRequest implements DeleteRequest {
  @JsonProperty(required = true)
  @Min(value = 0)
  private long issueId;
}
