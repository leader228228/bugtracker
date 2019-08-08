package ua.edu.sumdu.nc;

public class Utils {
    public static final String QUERY_SELECT_ISSUE_BY_ID = "select * from bt_issues where issue_id = ?";
    public static final String QUERY_SELECT_USER_BY_ID = "select * from bt_users where user_id = ?";
    public static final String QUERY_SELECT_ISS_STATUS_BY_ID = "select * from bt_issue_statuses where status_id = ?"; // TODO check the table name bt_issue_statuses
    public static final String QUERY_SELECT_PROJECT_BY_ID = "select * from bt_projects where project_id = ?";
}
