package ua.edu.sumdu.nc;

import dao.DAO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class Utils {
    private ApplicationContext applicationContext;
    private DAO dao;
    public static final String QUERY_SELECT_ISSUE_BY_ID = "select * from bt_issues where issue_id = ?";
    public static final String QUERY_SELECT_ISSUES_BY_ID = "select * from bt_issues where issue_id in ?";
    public static final String QUERY_SELECT_USER_BY_ID = "select * from bt_users where user_id = ?";
    public static final String QUERY_SELECT_ISS_STATUS_BY_ID = "select * from bt_issue_statuses where status_id = ?"; // TODO check the table name bt_issue_statuses
    public static final String QUERY_SELECT_PROJECT_BY_ID = "select * from bt_projects where project_id = ?";
    public static final String QUERY_GET_NEXT_ID = "select getId() id from dual";
    public static final String QUERY_INSERT_ISSUE =
        "insert into" +
            " bt_issues (TITLE, BODY, REPORTER_ID, ASSIGNEE_ID, CREATED, STATUS_ID, PROJECT_ID)" +
        " values " +
            "(?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_ISSUE_QUERY =
        "update " +
            "bt_issues " +
        "set " +
            "title = ?, " +
            "body = ?, " +
            "reporter_id = ?, " +
            "assignee_id = ?, " +
            "created = ?, " +
            "status_id = ?, " +
            "project_id = ? " +
            "where " +
            "issue_id = ?";

    public Utils(@Autowired ApplicationContext applicationContext, @Autowired @Qualifier(value = "DAO") DAO dao) {
        this.applicationContext = applicationContext;
        this.dao = dao;
    }

    public long getId() throws SQLException {
        try (Connection connection = dao.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY_GET_NEXT_ID);
            return preparedStatement.executeQuery().getLong("id");
        }
    }
}
