package filters;

import dao.DAO;
import dbparsers.DBParser;

import java.sql.SQLException;
import java.util.Collection;

public abstract class Filter<T> {
    protected DBParser<T> parser;
    protected DAO dao;

    public Filter(DBParser<T> parser, DAO dao) {
        this.parser = parser;
        this.dao = dao;
    }

    public abstract Collection<T> execute() throws SQLException;
}

