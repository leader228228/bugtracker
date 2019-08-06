package entities.bt;

import dao.impl.DAOImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Issue extends Entity {
    long getIssueId();
    long getReporterId();
    void setReporterId(long reporterId);
    long getAssigneeId();
    void setAssigneeId(long assigneeId);
    String getBody();
    String getTitle();
    void setBody(String body);
    Date getCreated();
    void setCreated(Date created);
    int getStatusId();
    void setStatusId(int statusId);
    long getProjectId();
    void setProjectId(long projectId);
    void setIssueId(long issueId);
    void setTitle(String title);

    @Override
    default void updateOrSave() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement;
            try {
                 preparedStatement = connection.prepareStatement("INSERT INTO BT_ISSUES("
                                + "ISSUE_ID, REPORTER_ID, ASSIGNEE_ID, CREATED, STATUS_ID, PROJECT_ID, BODY, TITLE)"
                                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setLong(1, getIssueId());
                preparedStatement.setLong(2, getReporterId());
                preparedStatement.setLong(3, getAssigneeId());
                preparedStatement.setDate(4, getCreated());
                preparedStatement.setInt(5, getStatusId());
                preparedStatement.setLong(6, getProjectId());
                preparedStatement.setString(7, getBody());
                preparedStatement.setString(8, getTitle());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                preparedStatement = connection.prepareStatement("UPDATE BT_ISSUES SET " +
                        "REPORTER_ID = ?" +
                        ", ASSIGNEE_ID = ?" +
                        ", CREATED = ?" +
                        ", STATUS_ID = ?" +
                        ", PROJECT_ID = ?" +
                        ", BODY = ?" +
                        ", TITLE = ?" +
                        " WHERE ISSUE_ID = ?;");
                preparedStatement.setLong(1, getReporterId());
                preparedStatement.setLong(2, getAssigneeId());
                preparedStatement.setDate(3, getCreated());
                preparedStatement.setInt(4, getStatusId());
                preparedStatement.setLong(5, getProjectId());
                preparedStatement.setString(6, getBody());
                preparedStatement.setString(7, getTitle());
                preparedStatement.setLong(8, getIssueId());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    default void delete() throws SQLException, IOException {
        try (Connection connection = new DAOImpl().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BT_ISSUES WHERE ISSUE_ID = ?;");
            preparedStatement.setLong(1, getIssueId());
            preparedStatement.execute();
        }
    }
}
