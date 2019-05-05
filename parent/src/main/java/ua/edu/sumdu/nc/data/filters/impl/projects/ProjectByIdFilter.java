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
public class ProjectByIdFilter extends ProjectFilter {
    private long [] projectId;

    public ProjectByIdFilter(DAO dao) {
        super(dao); // TODO replace with context.getBean()
    }

    public ProjectByIdFilter(DAO dao, long...projectId) {
        super(dao);
        this.projectId = projectId;
    }

    public long[] getProjectId() {
        return projectId;
    }

    public void setProjectId(long[] projectId) {
        this.projectId = projectId;
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
            return new AllProjectsParser().parse(preparedStatement.executeQuery());
        }
    }
}