package ua.edu.sumdu.nc.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Project implements Entity {
    private long projectID;
    private String name;
}
