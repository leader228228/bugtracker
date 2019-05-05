package ua.edu.sumdu.nc.data.entities.bt;

import ua.edu.sumdu.nc.data.dao.impl.DAOImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface User extends Entity {
    long getUserId();
    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    String getLogin();
    void setLogin(String login);
    String getPassword();
    void setPassword(String password);

    @Override
    default void updateOrSave() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connection.prepareStatement("INSERT INTO BT_USERS ("
                        + "USER_ID, LOGIN, PASSWORD, FIRST_NAME, LAST_NAME) "
                        + "VALUES (?, ?, ?, ?, ?);");
                preparedStatement.setLong(1, getUserId());
                preparedStatement.setString(2, getLogin());
                preparedStatement.setString(3, getPassword());
                preparedStatement.setString(4, getFirstName());
                preparedStatement.setString(5, getLastName());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                preparedStatement = connection.prepareStatement("UPDATE BT_USERS SET " +
                        "LOGIN = ? " +
                        ", PASSWORD = ? " +
                        ", FIRST_NAME = ? " +
                        ", LAST_NAME = ? " +
                        "WHERE USER_ID = ?;");
                preparedStatement.setString(1, getLogin());
                preparedStatement.setString(2, getPassword());
                preparedStatement.setString(3, getFirstName());
                preparedStatement.setString(4, getLastName());
                preparedStatement.setLong(5, getUserId());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    default void delete() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_USERS WHERE USER_ID = ?;");
            preparedStatement.setLong(1, getUserId());
            preparedStatement.execute();
        }
    }
}
