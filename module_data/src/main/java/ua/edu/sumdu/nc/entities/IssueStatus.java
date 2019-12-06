package ua.edu.sumdu.nc.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IssueStatus implements Entity {
    private int statusID;
    private String value;
}