package ua.edu.sumdu.nc.validation.replies;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.sumdu.nc.validation.BTRequest;

import java.sql.Date;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchRepliesRequest implements BTRequest {
    private long [] replyIds;
    private String bodyRegexp;
    private long [] issueIds;
    private long [] authorIds;
    private Date from;
    private Date to;
}
