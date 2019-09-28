package dao;

import entities.bt.Entity;
import entities.bt.Issue;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public interface DAO {
  Connection getConnection() throws SQLException;
  default long getId() throws SQLException {
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
                 "select getId() as \"id\" from dual")) {
      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();
      return resultSet.getLong("id");
    }
  }
}