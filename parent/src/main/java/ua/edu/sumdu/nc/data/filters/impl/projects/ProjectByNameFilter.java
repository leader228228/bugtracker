package ua.edu.sumdu.nc.data.filters.impl.projects;

import ua.edu.sumdu.nc.data.dao.DAO;
import ua.edu.sumdu.nc.data.entities.bt.Project;
import ua.edu.sumdu.nc.data.parsers.Parser;
import ua.edu.sumdu.nc.data.parsers.impl.projects.AllProjectsParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class ProjectByNameFilter extends ProjectFilter {
    private String name;
    private boolean isStrict;

    public ProjectByNameFilter(Parser<Project> parser, DAO dao) {
        super(parser, dao);
    }

    public Parser<Project> getParser() {
        return parser;
    }

    public void setParser(Parser<Project> parser) {
        this.parser = parser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStrict() {
        return isStrict;
    }

    public void setStrict(boolean strict) {
        isStrict = strict;
    }

    @Override
    public Collection<Project> execute() throws SQLException {
        try (Connection connection = dao.getConnection()) {
            PreparedStatement preparedStatement;
            if (isStrict) {
                preparedStatement = connection.prepareStatement(
                        "SELECT * FROM BT_PROJECTS WHERE NAME = ?;");
            } else {
                preparedStatement = connection.prepareStatement(
                        "SELECT * FROM BT_PROJECTS WHERE CONTAINS(NAME, ?);");
            }
            return parser.parse(preparedStatement.executeQuery());
        }
    }
}
