package ua.edu.sumdu.nc.db.dbparsers;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.Collection;

public interface DBParser<T> {
    Collection<T> parse(ResultSet resultSet) throws Exception;
}
