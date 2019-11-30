package services.users;

import entities.EntityFactory;
import entities.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import services.DBUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/** @noinspection Duplicates*/
@Service
public class UserService {

    private DataSource dataSource;

    public UserService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void saveUser(User user) throws SQLException {
        String insertUserQuery = "insert into bt_users(login, password, first_name, last_name) values(?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.executeUpdate();
        }
    }

    public User createUser(String firstName, String lastName, String login, String password) throws SQLException {
        User user = EntityFactory.get(User.class);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setPassword(String.valueOf(password.hashCode()));
        saveUser(user);
        return user;
    }

    public Collection<User> getAll() throws SQLException {
        String selectAllUsersQuery = "select * from bt_users";
        Collection<User> allUsers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.prepareStatement(selectAllUsersQuery).executeQuery()) {
            while (resultSet.next()) {
                allUsers.add(DBUtils.readUser(resultSet));
            }
        }
        return allUsers;
    }

    public Collection<User> searchUsersByIDs(long [] userIDs) throws SQLException {
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
                users.add(DBUtils.readUser(resultSet));
            }
        }
        return users;
    }

    /**
     * @param           name a part either of a first either of a last user name, case is insensitive
     *                           if {@code userName} is empty or {@code null}, then all users are returned
     * */
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
                    users.add(DBUtils.readUser(resultSet));
                }
            }
        }
        return users;
    }

    public void deleteUsers(long [] userIDs) throws SQLException {
        if (userIDs.length == 0) {
            return;
        }
        String _userIDs = Arrays.toString(userIDs);
        String deleteUsersQuery =
            "delete from bt_users where user_id in (" + _userIDs.substring(1, _userIDs.length() -1) + ")";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteUsersQuery)) {
            preparedStatement.executeUpdate();
        }
    }

    public User hideCredentials(User user) {
        return new User() {

            @Override
            public long getUserID() {
                return user.getUserID();
            }

            @Override
            public String getFirstName() {
                return user.getFirstName();
            }

            @Override
            public void setFirstName(String firstName) {
                throw new UnsupportedOperationException("This object is a read-only user view");
            }

            @Override
            public String getLastName() {
                return user.getLastName();
            }

            @Override
            public void setLastName(String lastName) {
                throw new UnsupportedOperationException("This object is a read-only user view");
            }

            @Override
            public String getLogin() {
                return "**********";
            }

            @Override
            public void setLogin(String login) {
                throw new UnsupportedOperationException("This object is a read-only user view");
            }

            @Override
            public String getPassword() {
                return "**********";
            }

            @Override
            public void setPassword(String password) {
                throw new UnsupportedOperationException("This object is a read-only user view");
            }

            @Override
            public void setUserID(long userId) {
                throw new UnsupportedOperationException("This object is a read-only user view");
            }
        };
    }
}
