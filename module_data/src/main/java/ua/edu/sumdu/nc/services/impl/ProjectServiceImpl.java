package ua.edu.sumdu.nc.services.impl;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.entities.Project;
import ua.edu.sumdu.nc.services.DBUtils;
import ua.edu.sumdu.nc.services.ProjectService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final Logger logger = Logger.getLogger(ProjectServiceImpl.class);

    private final DataSource dataSource;

    public ProjectServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static Project readProject(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setName(resultSet.getString("name"));
        project.setProjectID(resultSet.getInt("project_id"));
        return project;
    }

    @Override
    public Project createProject(String projectName) throws SQLException {
        Project project = new Project();
        project.setName(projectName);

        String saveQuery = "begin insert into bt_projects(name) values(?) returning project_id into ?; end;";
        try(Connection connection = dataSource.getConnection();
            OracleCallableStatement callableStatement = (OracleCallableStatement) connection.prepareCall(saveQuery)) {
            callableStatement.registerOutParameter(2, OracleTypes.INTEGER);
            callableStatement.setString(1, project.getName());
            callableStatement.execute();
            project.setProjectID(callableStatement.getInt(2));
            logger.info("Project id = " + project.getProjectID() + " has been successfully saved");
        }
        return project;
    }

    @Override
    public Collection<Project> getAll() throws SQLException {
        String getAllProjects = "select * from bt_projects";
        Collection<Project> allProjects = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement(getAllProjects).executeQuery()) {
            while (resultSet.next()) {
                allProjects.add(readProject(resultSet));
            }
        }
        return allProjects;
    }

    @Override
    public Collection<Project> searchProjectsByIDs(long[] projectIDs) throws SQLException {
        String projectsIDs = Arrays.toString(projectIDs);
        String getProjectsByIDs =
            "select * from bt_projects where project_id in (" + projectsIDs.substring(1, projectsIDs.length() - 1) + ")";
        Collection<Project> projects = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement(getProjectsByIDs).executeQuery()) {
            while (resultSet.next()) {
                projects.add(readProject(resultSet));
            }
        }
        return projects;
    }

    @Override
    public Collection<Project> searchProjectsByName(String projectName) throws SQLException {
        String getProjectsQuery =
            "select * from bt_projects where lower(name) like ?";
        Collection<Project> projects = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getProjectsQuery)) {
            preparedStatement.setString(1, DBUtils.getPatternContains(projectName.toLowerCase()));
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    projects.add(readProject(resultSet));
                }
            }
        }
        return projects;
    }

    @Override
    public boolean deleteProject(long projectID) throws SQLException {
        String deleteProjectQuery = "delete from bt_projects where project_id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteProjectQuery)) {
            preparedStatement.setLong(1, projectID);
            return preparedStatement.executeUpdate() != 0;
        }
    }
}
