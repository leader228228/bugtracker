package ua.edu.sumdu.nc.data.filters.impl.projects;

import ua.edu.sumdu.nc.data.dao.DAO;
import ua.edu.sumdu.nc.data.entities.bt.Project;
import ua.edu.sumdu.nc.data.filters.Filter;
import ua.edu.sumdu.nc.data.parsers.Parser;

abstract class ProjectFilter extends Filter<Project> {
    public ProjectFilter(Parser<Project> parser, DAO dao) {
        super(parser, dao);
    }
}