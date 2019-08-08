package filters.impl.projects;

import dao.DAO;
import entities.bt.Project;
import filters.Filter;
import dbparsers.DBParser;

abstract class ProjectFilter extends Filter<Project> {
    public ProjectFilter(DBParser<Project> parser, DAO dao) {
        super(parser, dao);
    }
}