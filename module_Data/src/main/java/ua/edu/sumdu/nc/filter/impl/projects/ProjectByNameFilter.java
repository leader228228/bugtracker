package ua.edu.sumdu.nc.filter.impl.projects;

import ua.edu.sumdu.nc.dao.DAO;
import ua.edu.sumdu.nc.entities.bt.Issue;
import ua.edu.sumdu.nc.entities.bt.Project;
import ua.edu.sumdu.nc.parsers.impl.projects.AllProjectsParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.regex.Pattern;

public class ProjectByNameFilter extends ProjectFileter {
    private String name;

    public ProjectByNameFilter(DAO dao, String name, boolean isStrict) {
        super(dao);
        this.name = isStrict ? '^' + Pattern.quote(name) + '$' : '%' + Pattern.quote(name) + '%';
    }

    @Override
    public Collection<Project> execute() throws SQLException {
        try (Connection connection = dao.getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM BT_PROJECTS WHERE NAME LIKE ?");
            preparedStatement.setString(1, name);
            return new AllProjectsParser().parse(preparedStatement.executeQuery());
        }
    }
}