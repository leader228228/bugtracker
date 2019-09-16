package dao;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public interface DAO {
    Connection getConnection() throws SQLException;
}