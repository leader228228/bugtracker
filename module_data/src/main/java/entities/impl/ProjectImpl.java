package entities.impl;

import dao.DAO;
import entities.bt.PersistanceEntity;
import entities.bt.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectImpl extends PersistanceEntity implements Project {
    private long projectId;
    private String name;
    private long adminId;

    public ProjectImpl(DAO DAO) {
        super(DAO);
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    @Override
    public void save() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO BT_PROJECTS ("
                     + "PROJECT_ID, \"name\", ADMIN_ID) "
                     + "VALUES (?, ?, ?)")) {
            preparedStatement.setLong(1, getProjectId());
            preparedStatement.setString(2, getName());
            preparedStatement.setLong(3, getAdminId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BT_PROJECTS SET " +
                     "NAME = ? " +
                     ", ADMIN_ID = ? " +
                     "WHERE PROJECT_ID = ?")) {
            preparedStatement.setString(1, getName());
            preparedStatement.setLong(2, getAdminId());
            preparedStatement.setLong(3, getProjectId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete() throws SQLException {
        try (Connection connection = DAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_PROJECTS WHERE PROJECT_ID = ?");
            preparedStatement.setLong(1, getProjectId());
            preparedStatement.execute();
        }
    }

    @Override
    public String toString() {
        return "ProjectImpl{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                ", adminId=" + adminId +
                '}';
    }
}
