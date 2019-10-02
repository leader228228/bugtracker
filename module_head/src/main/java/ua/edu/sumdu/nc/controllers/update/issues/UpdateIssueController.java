package ua.edu.sumdu.nc.controllers.update.issues;

import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.searchers.issues.IssueSearcher;
import ua.edu.sumdu.nc.validation.update.issues.UpdateIssueRequest;

import javax.validation.Valid;

@RestController
public class UpdateIssueController extends Controller<UpdateIssueRequest> {

    public UpdateIssueController(@Qualifier(value = "appConfig") ApplicationContext appCtx) {
        super(appCtx);
    }

    @Override
    public Object handle(UpdateIssueRequest request) {
        Issue issue = appCtx.getBean("IssueSearcher", IssueSearcher.class).getIssueByID(request.getIssueId());
        if (request.getAssigneeId() != null) {
            issue.setAssigneeId(request.getAssigneeId());
            logger.info("New assignee_id for issue (issue_id = "
                + request.getIssueId() + ") = " + request.getAssigneeId());
        }
        if (request.getBody() != null) {
            issue.setBody(request.getBody());
            logger.info("New body for issue (issue_id = "
                + request.getIssueId() + ") = " + request.getBody());
        }
        if (request.getTitle() != null) {
            issue.setTitle(request.getTitle());
            logger.info("New title for issue (issue_id = "
                + request.getIssueId() + ") = " + request.getTitle());
        }
        if (request.getProjectId() != null) {
            logger.info("New project_id for issue (issue_id = "
                + request.getIssueId() + ") = " + request.getProjectId());
            issue.setProjectId(request.getProjectId());
        }
        if (request.getStatusId() != null) {
            logger.info("New status_id for issue (issue_id = "
                + request.getIssueId() + ") = " + request.getStatusId());
            issue.setStatusId(request.getStatusId());
        }
        try {
            issue.update();
            logger.info("The issue (issue_id = " + request.getIssueId() + ") has been successfully updated");
            return getCommonSuccessResponse(
                "The issue (issue_id = " + request.getIssueId() + ") has been successfully updated");
        } catch (Exception e) {
            logger.error("The issue (issue_id = " + request.getIssueId() + ") updating was interrupted", e);
            return getCommonErrorResponse(
                "The issue (issue_id = " + request.getIssueId() + ") updating was interrupted", e.getMessage());
        }
    }

    @RequestMapping(
        path = "/update/issue",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object proxyMethod(@Valid @RequestBody UpdateIssueRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
