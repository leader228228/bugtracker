package ua.edu.sumdu.nc.validation.create.projects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import ua.edu.sumdu.nc.validation.create.CreateRequest;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateProjectRequest implements CreateRequest {
    @NotBlank
    @JsonProperty(required = true)
   private String name;
}
