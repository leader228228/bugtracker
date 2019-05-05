package ua.edu.sumdu.nc.data.filters.impl.issues;

import ua.edu.sumdu.nc.data.dao.DAO;
import ua.edu.sumdu.nc.data.entities.bt.Issue;
import ua.edu.sumdu.nc.data.filters.Filter;

abstract class IssueFilter implements Filter<Issue> {
    protected DAO dao;
    public IssueFilter(DAO dao) {
        this.dao = dao;
    }
}