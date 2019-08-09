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
    @Autowired
    private ApplicationContext applicationContext;
    private DAO dao;
    public static final String QUERY_SELECT_ISSUE_BY_ID = "select * from bt_issues where issue_id = ?";
    public static final String QUERY_SELECT_USER_BY_ID = "select * from bt_users where user_id = ?";
    public static final String QUERY_SELECT_ISS_STATUS_BY_ID = "select * from bt_issue_statuses where status_id = ?"; // TODO check the table name bt_issue_statuses
    public static final String QUERY_SELECT_PROJECT_BY_ID = "select * from bt_projects where project_id = ?";
    public static final String QUERY_GET_NEXT_ID = "select getId() id from dual";

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
