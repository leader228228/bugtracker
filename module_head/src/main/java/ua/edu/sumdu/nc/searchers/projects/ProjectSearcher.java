package ua.edu.sumdu.nc.searchers.projects;

import dao.DAO;
import entities.bt.Project;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
@Component(value = "ProjectSearcher")
@Scope(scopeName = "singleton")
public class ProjectSearcher {
    private ApplicationContext appCtx;
    private DAO DAO;
    private Logger logger =  Logger.getRootLogger();

    public ProjectSearcher(@Autowired DAO DAO, @Qualifier(value = "appConfig")ApplicationContext appCtx) {
        this.DAO = DAO;
        this.appCtx = appCtx;
    }

    public Project getProjectByID(long projectID) {
        final String getProjectByIDQuery = "select * from bt_projects where project_id = ?";
        try (
                Connection connection = DAO.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getProjectByIDQuery)
        ) {
            preparedStatement.setLong(1, projectID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Project project = appCtx.getBean("Project", Project.class);
                    project.setProjectId(resultSet.getLong("project_id"));
                    project.setName(resultSet.getString("name"));
                    return project;
                }
                logger.error("Can not find project (project_id = " + projectID + ")");
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
