package ua.edu.sumdu.nc.data.parsers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface Parser<T> {
    Collection<T> parse(ResultSet resultSet) throws SQLException;
}
