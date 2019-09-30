package ua.edu.sumdu.nc.validation.search.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.sumdu.nc.validation.search.SearchRequest;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchUsersRequest implements SearchRequest {
    private long [] userIds;
    private String firstNameRegexp;
    private String lastNamesRegexp;
}
