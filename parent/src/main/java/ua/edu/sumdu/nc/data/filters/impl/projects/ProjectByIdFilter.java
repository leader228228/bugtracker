package ua.edu.sumdu.nc.data.filters.impl.projects;

import ua.edu.sumdu.nc.data.dao.DAO;
import ua.edu.sumdu.nc.data.entities.bt.Project;
import ua.edu.sumdu.nc.data.parsers.Parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class ProjectByIdFilter extends ProjectFilter {
    private long [] projectId;
    private Parser<Project> parser;

    public ProjectByIdFilter(Parser<Project> parser, DAO dao) {
        super(parser, dao);
    }

    public long[] getProjectId() {
        return projectId;
    }

    public void setProjectId(long[] projectId) {
        this.projectId = projectId;
    }

    public Parser<Project> getParser() {
        return parser;
    }

    public void setParser(Parser<Project> parser) {
        this.parser = parser;
    }

    @Override
    public Collection<Project> execute() throws SQLException {
        try (Connection connection = dao.getConnection()) {
            Long[] longs = new Long[projectId.length];
            for (int i = 0; i < projectId.length; i++) {
                longs[i] = projectId[i];
            }
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM BT_PROJECTS WHERE PROJECT_ID IN ?");
            preparedStatement.setArray(1, connection.createArrayOf("NUMBER", longs));
            return parser.parse(preparedStatement.executeQuery());
        }
    }
}
