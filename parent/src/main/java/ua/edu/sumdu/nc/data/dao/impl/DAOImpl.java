package ua.edu.sumdu.nc.data.dao.impl;

import org.springframework.stereotype.Service;
import ua.edu.sumdu.nc.data.dao.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class DAOImpl implements DAO {
    // TODO REPLACE CONNECTION CONFIGURATION WITH PROPERTIES
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "SYSTEM", "A1q2w3e4r5t");
    }
}