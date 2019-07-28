package filters.impl.projects;

import dao.DAO;
import entities.bt.Project;
import filters.Filter;
import parsers.Parser;

abstract class ProjectFilter extends Filter<Project> {
    public ProjectFilter(Parser<Project> parser, DAO dao) {
        super(parser, dao);
    }
}