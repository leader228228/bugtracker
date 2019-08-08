package filters.impl.issues;

import dao.DAO;
import entities.bt.Issue;
import filters.Filter;
import dbparsers.DBParser;

abstract class IssueFilter extends Filter<Issue> {

    public IssueFilter(DBParser<Issue> parser, DAO dao) {
        super(parser, dao);
    }

    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }

    public DBParser<Issue> getParser() {
        return parser;
    }

    public void setParser(DBParser<Issue> parser) {
        this.parser = parser;
    }
}