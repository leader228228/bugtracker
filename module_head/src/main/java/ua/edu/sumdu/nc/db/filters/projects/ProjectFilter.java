package ua.edu.sumdu.nc.db.filters.projects;

import dao.DAO;
import entities.bt.Project;
import ua.edu.sumdu.nc.db.dbparsers.DBParser;
import ua.edu.sumdu.nc.db.filters.Filter;

abstract class ProjectFilter extends Filter<Project> {
    public ProjectFilter(DBParser<Project> parser, DAO dao) {
        super(parser, dao);
    }
}