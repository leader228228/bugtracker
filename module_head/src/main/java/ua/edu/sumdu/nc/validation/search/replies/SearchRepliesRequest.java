package ua.edu.sumdu.nc.validation.search.replies;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.sumdu.nc.validation.search.SearchRequest;

import java.sql.Date;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchRepliesRequest implements SearchRequest {
    private long [] replyIds;
    private String bodyRegexp;
    private long [] issueIds;
    private long [] authorIds;
    private Date from;
    private Date to;
}
