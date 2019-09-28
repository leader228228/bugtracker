package ua.edu.sumdu.nc.validation.delete.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.edu.sumdu.nc.validation.delete.DeleteRequest;

import javax.validation.constraints.Min;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DeleteUserRequest implements DeleteRequest {
  @JsonProperty(required = true)
  @Min(value = 1)
  private long userId;
}
