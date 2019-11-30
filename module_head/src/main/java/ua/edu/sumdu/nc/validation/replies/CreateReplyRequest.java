package ua.edu.sumdu.nc.validation.replies;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import ua.edu.sumdu.nc.validation.BTRequest;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateReplyRequest implements BTRequest {
  @JsonProperty(required = true)
  @NotBlank
  private String body;

  @JsonProperty(required = true)
  @Min(value = 1)
  private long issueID;

  @JsonProperty(required = true)
  @Min(value = 1)
  private long authorID;
}
