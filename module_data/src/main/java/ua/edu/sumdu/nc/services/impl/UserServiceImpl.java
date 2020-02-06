package ua.edu.sumdu.nc.services.impl;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.entities.User;
import ua.edu.sumdu.nc.services.DBUtils;
import ua.edu.sumdu.nc.services.UserService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/** @noinspection Duplicates*/
@Service
public class UserServiceImpl implements UserService {

    private DataSource dataSource;

    public UserServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static User readUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserID(resultSet.getLong("user_id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        return user;
    }

    @Override
    public User createUser(String firstName, String lastName, String login, String password) throws SQLException {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setPassword(String.valueOf(password.hashCode()));
        String insertUserQuery =
                "begin insert into bt_users(login, password, first_name, last_name) values(?, ?, ?, ?) " +
                        "returning user_id into ?; end;";
        try (Connection connection = dataSource.getConnection();
             OracleCallableStatement callableStatement =
                     (OracleCallableStatement) connection.prepareCall(insertUserQuery)) {
            callableStatement.registerOutParameter(5, OracleTypes.INTEGER);
            callableStatement.setString(1, user.getLogin());
            callableStatement.setString(2, user.getPassword());
            callableStatement.setString(3, user.getFirstName());
            callableStatement.setString(4, user.getLastName());
            callableStatement.execute();
            user.setUserID(callableStatement.getInt(5));
        }
        return user;
    }

    @Override
    public Collection<User> getAll() throws SQLException {
        String selectAllUsersQuery = "select * from bt_users";
        Collection<User> allUsers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.prepareStatement(selectAllUsersQuery).executeQuery()) {
            while (resultSet.next()) {
                allUsers.add(readUser(resultSet));
            }
        }
        return allUsers;
    }

    @Override
    public Collection<User> searchUsersByIDs(long[] userIDs) throws SQLException {
        if (userIDs.length == 0) {
            return getAll();
        }
        String _userIDs = Arrays.toString(userIDs);
        String selectUsersByIDs =
            "select * from bt_users where user_id in (" + _userIDs.substring(1, _userIDs.length() - 1) + ")";
        Collection<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.prepareStatement(selectUsersByIDs).executeQuery()) {
            while (resultSet.next()) {
                users.add(readUser(resultSet));
            }
        }
        return users;
    }

    @Override
    public Collection<User> searchUsersByName(String name) throws SQLException {
        if (StringUtils.isBlank(name)) {
            return getAll();
        }
        String selectUsersByName = "select * from bt_users where lower(first_name) like ? or lower(last_name) like ?";
        Collection<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectUsersByName)) {
            preparedStatement.setString(1, DBUtils.getPatternContains(name.toLowerCase()));
            preparedStatement.setString(2, DBUtils.getPatternContains(name.toLowerCase()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(readUser(resultSet));
                }
            }
        }
        return users;
    }

    @Override
    public boolean deleteUsers(long[] userIDs) throws SQLException {
        if (userIDs.length == 0) {
            return true;
        }
        String _userIDs = Arrays.toString(userIDs);
        String deleteUsersQuery =
            "delete from bt_users where user_id in (" + _userIDs.substring(1, _userIDs.length() -1) + ")";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteUsersQuery)) {
            return preparedStatement.executeUpdate() == userIDs.length;
        }
    }

    @Override
    public User searchUserByLogin(String login) throws SQLException {
        String query = "select * from bt_users where login = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return readUser(resultSet);
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public boolean updateUser(long userID, String firstName, String lastName, String login, String password)
        throws SQLException {
        Objects.requireNonNull(firstName, "First name can not be set to null");
        Objects.requireNonNull(lastName, "Last name can not be set to null");
        Objects.requireNonNull(login, "Login can not be set to null");
        Objects.requireNonNull(password, "Password can not be set to null");
        String updateQuery =
            "update bt_users set first_name = ?, last_name = ?, login = ?, password = ? where user_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, password);
            preparedStatement.setLong(5, userID);
            return preparedStatement.executeUpdate() != 0;
        }
    }
}
