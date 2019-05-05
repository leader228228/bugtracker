package ua.edu.sumdu.nc.data.filters.impl.projects;

import org.springframework.stereotype.Repository;
import ua.edu.sumdu.nc.data.dao.DAO;
import ua.edu.sumdu.nc.data.entities.bt.Project;
import ua.edu.sumdu.nc.data.parsers.impl.projects.AllProjectsParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class ProjectByNameFilter extends ProjectFilter {
    private String name;
    private boolean isStrict;

    public ProjectByNameFilter(DAO dao) {
        super(dao); // TODO replace with context.getBean()
    }

    public ProjectByNameFilter(DAO dao, String name, boolean isStrict) {
        super(dao);
        this.name = name;
        this.isStrict = isStrict;
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
            return new AllProjectsParser().parse(preparedStatement.executeQuery());
        }
    }
}
