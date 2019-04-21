package ua.edu.sumdu.nc.filter.impl.projects;

import ua.edu.sumdu.nc.dao.DAO;
import ua.edu.sumdu.nc.entities.bt.Project;
import ua.edu.sumdu.nc.parsers.impl.projects.AllProjectsParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class ProjectByNameFilter extends ProjectFileter {
    private String name;
    private boolean isStrict;

    public ProjectByNameFilter(DAO dao, String name, boolean isStrict) {
        super(dao);
        this.name = name;
        this.isStrict = isStrict;
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
            return new AllProjectsParser().parse(preparedStatement.executeQuery());
        }
    }
}
