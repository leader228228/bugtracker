package entities.bt;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

@Component
public interface Entity {
    void update() throws Exception;
    void save() throws Exception;
    void delete() throws Exception;
}
