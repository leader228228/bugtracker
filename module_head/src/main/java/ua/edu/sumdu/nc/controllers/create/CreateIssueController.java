package ua.edu.sumdu.nc.controllers.create;

import dao.DAO;
import entities.bt.Issue;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.everit.json.schema.Schema;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Timestamp;

@RestController
public class CreateIssueController extends Controller {
    private ApplicationContext applicationContext;
    private Object response;
    private Issue issue;
    private DAO DAO;
    private static final int DEFAULT_ISSUE_STATUS_ID = 2; //todo move to property file
    private final Logger logger = Logger.getRootLogger();

    public CreateIssueController(@Qualifier("appConfig") @Autowired ApplicationContext applicationContext, @Autowired DAO DAO, @Autowired Schema schema) {
        super(schema);
        this.applicationContext = applicationContext;
        this.DAO = DAO;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/create/issue")
    @ResponseBody
    public Object handle(HttpServletRequest request) throws Exception {
        String requestBody = IOUtils.toString(request.getInputStream());
        if (!isRequestBodyValid(requestBody)) {
            logger.error("Invalid request body : " + requestBody);
            return ERROR_RESPONSE_TEMPLATE.replaceFirst("#message#", "invalid request body");
        }
        JSONObject jsonObject = new JSONObject(requestBody);
        issue = applicationContext.getBean("Issue", Issue.class);
        issue.setProjectId(jsonObject.getLong("projectId"));
        try {
            issue.setAssigneeId(jsonObject.getInt("assigneeId"));
        } catch (JSONException e) {
            logger.info("Unspecified assignee id for issue. Request: " + jsonObject.toString());
        }
        issue.setReporterId(jsonObject.getLong("reporterId"));
        issue.setBody(jsonObject.getString("body"));
        issue.setTitle(jsonObject.getString("title"));
        try {
            issue.setCreated(convertDateFromJson(jsonObject.getString("created")));
        } catch (JSONException e) {
            logger.info("Unspecified creation date for issue. Request: " + jsonObject.toString());
            issue.setCreated(new Timestamp(System.currentTimeMillis()));
        }
        try {
            issue.setStatusId(jsonObject.getInt("statusId"));
        } catch (JSONException e) {
            logger.info("Unspecified status id for issue. Request: " + jsonObject.toString());
            issue.setStatusId(DEFAULT_ISSUE_STATUS_ID);
        }
        issue.setIssueId(DAO.getId());
        try {
            issue.save();
        } catch (SQLException e) {
            response = ERROR_RESPONSE_TEMPLATE.replaceFirst(
                    "#message#", "Error due to access to database\n\r" + /*todo remove*/ e.getMessage() + "issue :\n\r" + issue.toString());
            return response;
        }
        response = SUCCESS_RESPONSE_TEMPLATE.replaceFirst("#message#",
                "The issue has been successfully created. issueId = " + issue.getIssueId());
        return response;
    }

    /**
     *  Converts {@code date} parameter which has {@code date-time} format (JSON Schema v7)
     *
     * <table>
     *  <tr>
     *      <td>The string must correspond the format</td><td>yyyy-MM-ddThh:mm:ss[+-]hh:mm</td>
     *  </tr>
     *  <tr>
     *      <td>Example of a valid date</td><td>2018-11-13T20:20:39+00:00<td/>
     *  </tr>
     * </table>
     * */
    private Timestamp convertDateFromJson(String date) {
        int year = Integer.valueOf(date.substring(0,4)); /*31536000000 ms*/
        int month = Integer.valueOf(date.substring(5,7)); /*2592000000 ms*/
        int day = Integer.valueOf(date.substring(8,10)); /*86400000 ms*/
        int hours = Integer.valueOf(date.substring(11,13)); /*3600000 ms*/
        int minutes = Integer.valueOf(date.substring(14,16)); /*60000 ms*/
        int seconds = Integer.valueOf(date.substring(17,19)); /*1000 ms*/
        int plusMinus = date.charAt(19) == '+' ? 1 : -1;
        int plusHours = Integer.valueOf(date.substring(20,22));
        int plusMinutes = Integer.valueOf(date.substring(23));
        long timestamp =
                year * 31536000000L +
                month * 2592000000L +
                day * 86400000 +
                hours * 3600000 +
                minutes * 60000 +
                seconds * 1000 +
                    ((plusMinus) * (plusHours * 3600000 + plusMinutes * 60000));
        return new Timestamp(timestamp);
    }
}
