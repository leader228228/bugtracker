package dao.impl;

import dao.DAO;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DAOImpl implements DAO {
    // TODO REPLACE CONNECTION CONFIGURATION WITH PROPERTIES
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "SYSTEM", "A1q2w3e4r5t");
    }
}