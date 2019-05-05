package ua.edu.sumdu.nc.data.entities.bt;

import ua.edu.sumdu.nc.data.dao.impl.DAOImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Project extends Entity {
    long getProjectId();
    String getName();
    void setName(String name);
    long getAdminId();
    void setAdminId(long adminId);

    @Override
    default void updateOrSave() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connection.prepareStatement("INSERT INTO BT_PROJECTS ("
                        + "PROJECT_ID, NAME, ADMIN_ID) "
                        + "VALUES (?, ?, ?);");
                preparedStatement.setLong(1, getProjectId());
                preparedStatement.setString(2, getName());
                preparedStatement.setLong(3, getAdminId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                preparedStatement = connection.prepareStatement("UPDATE BT_PROJECTS SET " +
                        "NAME = ? " +
                        ", ADMIN_ID = ? " +
                        "WHERE PROJECT_ID = ?;");
                preparedStatement.setString(1, getName());
                preparedStatement.setLong(2, getAdminId());
                preparedStatement.setLong(3, getProjectId());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    default void delete() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_PROJECTS WHERE PROJECT_ID = ?;");
            preparedStatement.setLong(1, getProjectId());
            preparedStatement.execute();
        }
    }
}