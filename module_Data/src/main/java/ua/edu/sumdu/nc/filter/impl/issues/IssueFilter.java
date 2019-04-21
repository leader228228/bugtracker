package ua.edu.sumdu.nc.filter.impl.issues;

import ua.edu.sumdu.nc.dao.DAO;
import ua.edu.sumdu.nc.entities.bt.Issue;
import ua.edu.sumdu.nc.filter.Filter;

abstract class IssueFilter implements Filter<Issue> {
    protected DAO dao;
    public IssueFilter(DAO dao) {
        this.dao = dao;
    }
}