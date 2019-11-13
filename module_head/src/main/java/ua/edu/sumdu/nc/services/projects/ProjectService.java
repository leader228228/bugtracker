package ua.edu.sumdu.nc.services.projects;

import entities.bt.Project;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.controllers.EntityFactory;
import ua.edu.sumdu.nc.controllers.Utils;
import ua.edu.sumdu.nc.validation.create.projects.CreateProjectRequest;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Service
public class ProjectService {

    private final Logger logger = Logger.getRootLogger();

    private final DataSource dataSource;

    public ProjectService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void saveProject(Project project) throws SQLException {
        String saveProjectQuery = "insert into bt_projects(name) values(?)";
        try(PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(saveProjectQuery)) {
            preparedStatement.setString(1, project.getName());
            preparedStatement.executeUpdate();
            if(logger.isInfoEnabled()) {
                logger.info("Project id = " + project.getProjectID() + " has been successfully saved");
            }
        }
    }

    public Project createProject(CreateProjectRequest request) throws SQLException {
        Project project = EntityFactory.get(Project.class);
        project.setName(request.getName());
        saveProject(project);
        return project;
    }

    public Collection<Project> getAll() throws SQLException {
        String getAllProjects = "select * from bt_projects";
        Collection<Project> allProjects = new ArrayList<>();
        try(ResultSet resultSet = dataSource.getConnection().prepareStatement(getAllProjects).executeQuery()) {
            while (resultSet.next()) {
                allProjects.add(Utils.readProject(resultSet));
            }
        }
        return allProjects;
    }

    public Collection<Project> getProjectsByIDs(long [] projectIDs) throws SQLException {
        String projectsIDs = Arrays.toString(projectIDs);
        String getProjectsByIDs =
            "select * from bt_projects where project_id in (" + projectsIDs.substring(1, projectsIDs.length() - 1) + ")";
        Collection<Project> projects = new ArrayList<>();
        try(ResultSet resultSet = dataSource.getConnection().prepareStatement(getProjectsByIDs).executeQuery()) {
            while (resultSet.next()) {
                projects.add(Utils.readProject(resultSet));
            }
        }
        return projects;
    }
}
