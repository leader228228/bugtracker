package ua.edu.sumdu.nc.validation.issues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.sumdu.nc.validation.BTRequest;

import java.sql.Date;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SearchIssuesRequest implements BTRequest {
    private long [] issueIDs;
    private long [] reporterIDs;
    private long [] assigneeIDs;
    private String titleRegexp;
    private String bodyRegexp;
    private int [] statusID;
    private long [] projectIDs;
    private Date from;
    private Date to;
    private String replyBodyRegExp;
}
