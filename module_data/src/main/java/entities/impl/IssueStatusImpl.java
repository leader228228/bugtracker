package entities.impl;

import dao.DAO;
import entities.bt.IssueStatus;
import entities.bt.PersistanceEntity;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class IssueStatusImpl extends PersistanceEntity implements IssueStatus {
    private int statusId;
    private String value;

    public IssueStatusImpl(DAO DAO) {
        super(DAO);
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @Override
    public int getStatusId() {
        return statusId;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String newValue) {
        value = newValue;
    }

    @Override
    public void save() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO BT_ISSUE_STATUSES ("
                     + "STATUS_ID, VALUE) "
                     + "VALUES (?, ?)")) {
            preparedStatement.setLong(1, getStatusId());
            preparedStatement.setString(2, getValue());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE BT_ISSUE_STATUSES SET " +
                     "VALUE = ? " +
                     "WHERE STATUS_ID = ?;")) {
            preparedStatement.setString(1, getValue());
            preparedStatement.setInt(2, getStatusId());
            preparedStatement.executeUpdate();
        }
    }


    @Override
    public void delete() throws SQLException {
        try (Connection connection = DAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_ISSUES WHERE ISSUE_ID = ?;");
            preparedStatement.setInt(1, getStatusId());
            preparedStatement.execute();
        }
    }

    @Override
    public String toString() {
        return "IssueStatusImpl{" +
                "statusId=" + statusId +
                ", value='" + value + '\'' +
                '}';
    }
}
