package ua.edu.sumdu.nc.services;

import ua.edu.sumdu.nc.entities.Project;

import java.sql.SQLException;
import java.util.Collection;

public interface ProjectService {

    Project createProject(String projectName) throws SQLException;

    Collection<Project> getAll() throws SQLException;

    Collection<Project> searchProjectsByIDs(long[] projectIDs) throws SQLException;

    Collection<Project> searchProjectsByName(String projectName) throws SQLException;

    boolean deleteProject(long projectID) throws SQLException;
}
