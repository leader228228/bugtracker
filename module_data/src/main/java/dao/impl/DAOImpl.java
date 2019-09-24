package dao.impl;

import dao.DAO;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DAOImpl implements DAO {
    private final String URL;
    private final String user;
    private final String password;

    public DAOImpl(String URL, String user, String password) {
        this.URL = URL;
        this.user = user;
        this.password = password;
    }

    // TODO REPLACE CONNECTION CONFIGURATION WITH PROPERTIES
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, user, password);
    }
}