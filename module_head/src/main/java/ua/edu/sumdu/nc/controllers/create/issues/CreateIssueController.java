package ua.edu.sumdu.nc.controllers.create.issues;

import dao.DAO;
import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;

import java.sql.Date;
import java.sql.SQLException;

@Validated
@RestController
public class CreateIssueController extends Controller<CreateIssueRequest> {

    public CreateIssueController(@Qualifier(value = "appConfig") ApplicationContext appCtx, DAO DAO, Utils utils) {
        super(appCtx, DAO, utils);
    }

    @Override
    public Object handle(CreateIssueRequest request) {
        Issue issue = utils.getIssue();
        issue.setStatusId(request.getStatusId());
        try {
            issue.setIssueId(DAO.getId());
        } catch (SQLException e) {
            logger.error("Unknown error while setting a new id to issue, request=(" + request + ")", e);
            return getCommonErrorResponse("Error due to access to database");
        }
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
            return getCommonErrorResponse("Error due to access to database: ", e.getClass().toString());
        }
        return getCommonSuccessResponse("The issue id = " + issue.getIssueId() + " has been created");
    }

    @RequestMapping(
        path = "/create/issue",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object delegateMethod(@RequestBody CreateIssueRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
