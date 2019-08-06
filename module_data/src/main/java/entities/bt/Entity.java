package entities.bt;

import java.io.IOException;
import java.sql.SQLException;

public interface Entity {
    void updateOrSave() throws SQLException, IOException;
    void delete() throws SQLException, IOException;
}
