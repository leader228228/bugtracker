package entities.impl;

import dao.DAO;
import entities.bt.IssueStatus;
import entities.bt.PersistenceEntity;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class IssueStatusImpl extends PersistenceEntity implements IssueStatus {
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
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "insert into bt_issue_statuses (status_id, value) values (?, ?)")) {
            preparedStatement.setLong(1, getStatusId());
            preparedStatement.setString(2, getValue());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update() throws SQLException {
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                 "update bt_issue_statuses set value = ? where status_id = ?")) {
            preparedStatement.setString(1, getValue());
            preparedStatement.setInt(2, getStatusId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete() throws SQLException {
        try (Connection connection = DAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from bt_issue_statuses where issue_id = ?");
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
