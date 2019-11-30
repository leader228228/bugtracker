package ua.edu.sumdu.nc.validation.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.sumdu.nc.validation.BTRequest;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchUsersRequest implements BTRequest {
    private long [] userIds;
    private String firstNameRegexp;
    private String lastNamesRegexp;
}
