package filters;

import dao.DAO;
import parsers.Parser;

import java.sql.SQLException;
import java.util.Collection;

public abstract class Filter<T> {
    protected Parser<T> parser;
    protected DAO dao;

    public Filter(Parser<T> parser, DAO dao) {
        this.parser = parser;
        this.dao = dao;
    }

    public abstract Collection<T> execute() throws SQLException;
}

