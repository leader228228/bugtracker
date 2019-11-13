package entities.impl;

import entities.bt.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectImpl implements Project {
    private long projectId;
    private String name;

    public ProjectImpl() {
    }

    public void setProjectID(long projectId) {
        this.projectId = projectId;
    }

    public long getProjectID() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void save() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO BT_PROJECTS("
                 + "PROJECT_ID, name) "
                 + "VALUES (?, ?)")) {
            preparedStatement.setLong(1, getProjectID());
            preparedStatement.setString(2, getName());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BT_PROJECTS SET " +
                     "NAME = ? " +
                     "WHERE PROJECT_ID = ?")) {
            preparedStatement.setString(1, getName());
            preparedStatement.setLong(2, getProjectID());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete() throws SQLException {
        try (Connection connection = DAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_PROJECTS WHERE PROJECT_ID = ?");
            preparedStatement.setLong(1, getProjectID());
            preparedStatement.execute();
        }
    }

    @Override
    public String toString() {
        return "ProjectImpl{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                '}';
    }
}
