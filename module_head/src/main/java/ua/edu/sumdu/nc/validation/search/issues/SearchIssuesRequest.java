package ua.edu.sumdu.nc.validation.search.issues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.sumdu.nc.validation.search.SearchRequest;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchIssuesRequest implements SearchRequest {
    private long [] issueIds;
    private long [] reporterIds;
    private long [] assigneeIds;
    private String titleRegexp;
    private String bodyRegexp;
    private int [] statusId;
    private long [] projectIds;
    private Timestamp [] created;
}
