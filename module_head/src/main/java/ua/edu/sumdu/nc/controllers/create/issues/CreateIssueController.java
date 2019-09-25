package ua.edu.sumdu.nc.controllers.create.issues;

import dao.DAO;
import entities.bt.Issue;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

@RestController
public class CreateIssueController extends Controller {

    @Value(value = "${bt.defaults.issueStatus}")
    private int DEFAULT_ISSUE_STATUS_ID;

    public CreateIssueController(
            @Qualifier("appConfig") ApplicationContext applicationContext,
            @Autowired DAO DAO,
            @Autowired Schema schema
    ) {
        super(schema, DAO, applicationContext);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/create/issue")
    @ResponseBody
    public Object handle(HttpServletRequest httpServletRequest) {
        Issue issue;
        String requestBody;
        try {
            requestBody = getRequestBodyAsString(httpServletRequest);
        } catch (IOException e) {
            logger.error("Unknown exception while reading request body", e);
            return getErrorResponseMessage("Internal error");
        }
        if (!isRequestBodyValid(requestBody)) {
            logger.error("Invalid request body : " + requestBody);
            return getErrorResponseMessage("Invalid request body");
        }
        JSONObject jsonObject = new JSONObject(requestBody);
        issue = applicationContext.getBean("Issue", Issue.class);
        issue.setProjectId(jsonObject.getLong("projectId"));
        if (jsonObject.has("assigneeId")) {
            issue.setAssigneeId(jsonObject.getInt("assigneeId"));
        } else {
            // default 0 both for long and column value
            logger.info("Unspecified assignee id for issue. Request: " + jsonObject.toString());
        }
        issue.setReporterId(jsonObject.getLong("reporterId"));
        issue.setBody(jsonObject.getString("body"));
        issue.setTitle(jsonObject.getString("title"));
        issue.setCreated(new Timestamp(System.currentTimeMillis()));
        if (jsonObject.has("statusId")) {
            issue.setStatusId(jsonObject.getInt("statusId"));
        } else {
            logger.info("Unspecified status id for issue. Request: " + jsonObject.toString());
            issue.setStatusId(DEFAULT_ISSUE_STATUS_ID);
        }
        try {
            issue.setIssueId(DAO.getId());
            issue.save();
        } catch (SQLException e) {
            logger.error("Error while creating new issue", e);
            return getErrorResponseMessage("Error due to access to database" + /*todo debug remove*/ System.lineSeparator() + e.getMessage() + "issue :\n\r" + issue.toString());
        }
        return getSuccessResponseMessage(
                "The issue has been successfully created. issueId = " + issue.getIssueId()
        );
    }
}
