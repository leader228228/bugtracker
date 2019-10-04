package ua.edu.sumdu.nc.controllers.delete.issues;

import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.searchers.issues.IssueSearcher;
import ua.edu.sumdu.nc.validation.delete.issues.DeleteIssueRequest;

@Validated
@RestController
public class DeleteIssueController extends Controller<DeleteIssueRequest> {

    public DeleteIssueController(@Qualifier(value = "appConfig") ApplicationContext appCtx) {
        super(appCtx);
    }

    @Override
    public Object handle(DeleteIssueRequest request) {
        return deleteIssue(request.getIssueId());
    }

    private Issue findIssueById(long issueId) {
        return appCtx.getBean("IssueSearcher", IssueSearcher.class).getIssueByID(issueId);
    }

    private Object deleteIssue(long issueId) {
        try {
            Issue issue = findIssueById(issueId);
            if (issue != null) {
                issue.delete();
                logger.info("Issue (ID = " + issueId + ") has been successfully deleted");
                return getCommonSuccessResponse(
                    "Issue (ID = " + issueId + ") has been successfully deleted");
            }
            logger.error("Can not find issue. Request = (" + issueId + ")");
            return getCommonErrorResponse("Can not find issue (ID = " + issueId + ")");
        } catch (Exception e) {
            logger.error("Error while issue deletion (ID = " + issueId + ")", e);
            return getCommonErrorResponse("Error while issue deletion (ID = " + issueId + ")");
        }
    }

    @RequestMapping(
            path = "/delete/issue",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public Object proxyMethod(@RequestBody DeleteIssueRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/delete/issue/{id}", produces = "application/json")
    public Object proxyMethod(@PathVariable(value = "id") long issueId) {
        return deleteIssue(issueId);
    }
}
