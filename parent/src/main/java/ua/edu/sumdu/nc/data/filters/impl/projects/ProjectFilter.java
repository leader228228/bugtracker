package ua.edu.sumdu.nc.data.filters.impl.projects;

import ua.edu.sumdu.nc.data.dao.DAO;
import ua.edu.sumdu.nc.data.entities.bt.Project;
import ua.edu.sumdu.nc.data.filters.Filter;

abstract class ProjectFilter implements Filter<Project> {
    protected DAO dao;

    public ProjectFilter(DAO dao) {
        this.dao = dao;
    }
}