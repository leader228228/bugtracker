package filters.impl.issues;

import dao.DAO;
import entities.bt.Issue;
import filters.Filter;
import parsers.Parser;

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