package ua.edu.sumdu.nc.data.filters.impl.issues;

import ua.edu.sumdu.nc.data.dao.DAO;
import ua.edu.sumdu.nc.data.entities.bt.Issue;
import ua.edu.sumdu.nc.data.filters.Filter;
import ua.edu.sumdu.nc.data.parsers.Parser;

abstract class IssueFilter extends Filter<Issue> {

    public IssueFilter(Parser<Issue> parser, DAO dao) {
        super(parser, dao);
    }

    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }

    public Parser<Issue> getParser() {
        return parser;
    }

    public void setParser(Parser<Issue> parser) {
        this.parser = parser;
    }
}