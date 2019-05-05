package ua.edu.sumdu.nc.data.entities.bt;

import ua.edu.sumdu.nc.data.dao.impl.DAOImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Session extends Entity {
    long getSesionId();
    long getUserId();
    void setUserId(long userId);
    Date getFromDate();
    void setFromDate(Date fromDate);
    Date getTillDate();
    void setTillDate(Date tillDate);
    String getToken();
    void setToken(String token);
    void setSesionId(long sesionId);

    @Override
    default void updateOrSave() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connection.prepareStatement("INSERT INTO BT_USER_SESSIONS ("
                        + "SESSION_ID, USER_ID, FROM_DATE, TILL_DATE, TOKEN) "
                        + "VALUES (?, ?, ?, ?, ?);");
                preparedStatement.setLong(1, getSesionId());
                preparedStatement.setLong(2, getUserId());
                preparedStatement.setDate(3, getFromDate());
                preparedStatement.setDate(4, getTillDate());
                preparedStatement.setString(5, getToken());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                preparedStatement = connection.prepareStatement("UPDATE BT_USER_SESSIONS SET " +
                        "USER_ID = ? " +
                        ", FROM_DATE = ? " +
                        ", TILL_DATE = ? " +
                        ", TOKEN = ? " +
                        "WHERE SESSION_ID = ?;");
                preparedStatement.setLong(1, getUserId());
                preparedStatement.setDate(2, getFromDate());
                preparedStatement.setDate(3, getTillDate());
                preparedStatement.setString(4, getToken());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    default void delete() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_USER_SESSIONS WHERE SESSION_ID = ?;");
            preparedStatement.setLong(1, getSesionId());
            preparedStatement.execute();
        }
    }
}