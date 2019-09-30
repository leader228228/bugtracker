package entities.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dao.DAO;
import entities.bt.PersistanceEntity;
import entities.bt.User;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class UserImpl extends PersistanceEntity implements User {
    private long userId;
    private String firstName;
    private String lastName;
    private String login;
    private String password;

    public UserImpl(DAO DAO) {
        super(DAO);
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void save() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO BT_USERS ("
                             + "USER_ID, LOGIN, PASSWORD, FIRST_NAME, LAST_NAME) "
                             + "VALUES (?, ?, ?, ?, ?)")){
            preparedStatement.setLong(1, getUserId());
            preparedStatement.setString(2, getLogin());
            preparedStatement.setString(3, getPassword());
            preparedStatement.setString(4, getFirstName());
            preparedStatement.setString(5, getLastName());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE BT_USERS SET " +
                             "LOGIN = ? " +
                             ", PASSWORD = ? " +
                             ", FIRST_NAME = ? " +
                             ", LAST_NAME = ? " +
                             "WHERE USER_ID = ?")) {
            preparedStatement.setString(1, getLogin());
            preparedStatement.setString(2, getPassword());
            preparedStatement.setString(3, getFirstName());
            preparedStatement.setString(4, getLastName());
            preparedStatement.setLong(5, getUserId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete() throws SQLException {
        try (Connection connection = DAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_USERS WHERE USER_ID = ?");
            preparedStatement.setLong(1, getUserId());
            preparedStatement.execute();
        }
    }

    @Override
    public String toString() {
        return "UserImpl{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
