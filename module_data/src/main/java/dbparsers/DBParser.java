package dbparsers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface DBParser<T> {
    Collection<T> parse(ResultSet resultSet) throws SQLException;
}
