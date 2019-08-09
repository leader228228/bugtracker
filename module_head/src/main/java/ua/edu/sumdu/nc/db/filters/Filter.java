package ua.edu.sumdu.nc.db.filters;

import dao.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.db.dbparsers.DBParser;

import java.util.Collection;

@Service
public abstract class Filter<T> {
    protected DBParser<T> parser;
    protected DAO dao;

    public Filter(DBParser<T> parser, @Autowired DAO dao) {
        this.parser = parser;
        this.dao = dao;
    }

    public abstract Collection<T> execute() throws Exception;
}

