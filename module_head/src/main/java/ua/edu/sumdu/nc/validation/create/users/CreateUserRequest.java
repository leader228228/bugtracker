package ua.edu.sumdu.nc.validation.create.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import ua.edu.sumdu.nc.validation.BTRequest;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CreateUserRequest implements BTRequest {

  @JsonProperty(required = true)
  @NotBlank
  private String firstName;

  @JsonProperty(required = true)
  @NotBlank
  private String lastName;

  @JsonProperty(required = true)
  @NotBlank
  private String login;

  @JsonProperty(required = true)
  @NotBlank
  private String password;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
