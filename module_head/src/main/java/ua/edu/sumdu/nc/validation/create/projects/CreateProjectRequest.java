package ua.edu.sumdu.nc.validation.create.projects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import ua.edu.sumdu.nc.validation.create.CreateRequest;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateProjectRequest implements CreateRequest {
    @NotBlank
    @JsonProperty(required = true)
   private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
