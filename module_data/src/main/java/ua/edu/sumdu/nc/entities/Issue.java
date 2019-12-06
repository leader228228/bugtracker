package ua.edu.sumdu.nc.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class Issue implements Entity {
    private long issueID;
    private long reporterID;
    private long assigneeID;
    private String title;
    private String body;
    private Date created;
    private int statusID;
    private long projectID;
}
