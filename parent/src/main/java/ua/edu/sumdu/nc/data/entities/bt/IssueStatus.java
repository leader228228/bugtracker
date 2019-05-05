package ua.edu.sumdu.nc.data.entities.bt;

import ua.edu.sumdu.nc.data.dao.impl.DAOImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IssueStatus extends Entity {
    int getStatusId();
    String getValue();
    void setValue(String newValue);

    @Override
    default void updateOrSave() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connection.prepareStatement("INSERT INTO BT_ISSUE_STATUSES ("
                        + "STATUS_ID, VALUE) "
                        + "VALUES (?, ?);");
                preparedStatement.setLong(1, getStatusId());
                preparedStatement.setString(2, getValue());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                preparedStatement = connection.prepareStatement("UPDATE BT_ISSUE_STATUSES SET " +
                        "VALUE = ? " +
                        "WHERE STATUS_ID = ?;");
                preparedStatement.setString(1, getValue());
                preparedStatement.setInt(2, getStatusId());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    default void delete() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_ISSUES WHERE ISSUE_ID = ?;");
            preparedStatement.setInt(1, getStatusId());
            preparedStatement.execute();
        }
    }
}