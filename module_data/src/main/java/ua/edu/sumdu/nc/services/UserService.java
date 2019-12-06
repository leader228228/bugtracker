package ua.edu.sumdu.nc.services;

import ua.edu.sumdu.nc.entities.User;

import java.sql.SQLException;
import java.util.Collection;

public interface UserService {

    User createUser(String firstName, String lastName, String login, String password) throws SQLException;

    Collection<User> getAll() throws SQLException;

    Collection<User> searchUsersByIDs(long[] userIDs) throws SQLException;

    /**
     * @param           name a part either of a first either of a last user name, case is insensitive
     *                           if {@code userName} is empty or {@code null}, then all users are returned
     * */
    Collection<User> searchUsersByName(String name) throws SQLException;

    void deleteUsers(long[] userIDs) throws SQLException;

    User searchUserByLogin(String login) throws SQLException;

    void updateUser(long userID, String firstName, String lastName, String login, String password)
        throws SQLException;
}
