package ua.edu.sumdu.nc.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class Reply implements Entity {
    private long replyID;
    private String body;
    private long issueID;
    private long authorID;
    private Date created;
}
