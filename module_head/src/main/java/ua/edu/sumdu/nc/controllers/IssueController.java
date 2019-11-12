package ua.edu.sumdu.nc.controllers;

import entities.bt.Issue;
import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;

@Validated
@RestController(value = "/issues")
public class IssueController {

    private Logger logger = Logger.getRootLogger();

    @RequestMapping(
        path = "/create",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object createIssue(@RequestBody CreateIssueRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Invalid request: " + request.toString());
            return Utils.getInvalidRequestResponse(bindingResult);
        }
        Issue issue = EntityFactory.get(Issue.class);
        issue.setCreated(new Date(System.currentTimeMillis()));
        issue.setTitle(request.getTitle());
        issue.setAssigneeId(request.getAssigneeId());
        issue.setBody(request.getBody());
        issue.setProjectId(request.getProjectId());
        issue.setReporterId(request.getReporterId());
        try {
            issue.save();
        } catch (SQLException e) {
            logger.error("Unknown error while saving the issue, request=(" + request + ")", e);
            return Utils.getCommonErrorResponse("Error due to access to database: ", e.getClass().toString());
        }
        return Utils.getCommonSuccessResponse("The issue id = " + issue.getIssueId() + " has been created");
    }
}
