package entities.bt;

import java.sql.SQLException;

public interface Entity {
    void update() throws SQLException;
    void save() throws SQLException;
    void delete() throws SQLException;
}
