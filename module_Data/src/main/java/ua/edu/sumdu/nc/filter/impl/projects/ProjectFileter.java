package ua.edu.sumdu.nc.filter.impl.projects;

import ua.edu.sumdu.nc.dao.DAO;
import ua.edu.sumdu.nc.entities.bt.Project;
import ua.edu.sumdu.nc.filter.Filter;

abstract class ProjectFileter implements Filter<Project> {
    protected DAO dao;

    public ProjectFileter(DAO dao) {
        this.dao = dao;
    }
}