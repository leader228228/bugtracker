package entities.bt;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

@Component
public interface Entity {
    void updateOrSave() throws SQLException, IOException;
    void delete() throws SQLException, IOException;
}
