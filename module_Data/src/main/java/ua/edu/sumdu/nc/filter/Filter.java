package ua.edu.sumdu.nc.filter;

import java.sql.SQLException;
import java.util.Collection;

public interface Filter<T> {
    Collection<T> execute() throws SQLException;
}
