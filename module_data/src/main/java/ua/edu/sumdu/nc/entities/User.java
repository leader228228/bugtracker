package ua.edu.sumdu.nc.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User implements Entity {
    private long userID;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
}
