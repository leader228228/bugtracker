package ua.edu.sumdu.nc.validation.create.replies;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import ua.edu.sumdu.nc.validation.create.CreateRequest;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateReplyRequest implements CreateRequest {
  @JsonProperty(required = true)
  @NotBlank
  private String body;

  @JsonProperty(required = true)
  @Min(value = 1)
  private long issueId;

  @JsonProperty(required = true)
  @Min(value = 1)
  private long authorId;
}
