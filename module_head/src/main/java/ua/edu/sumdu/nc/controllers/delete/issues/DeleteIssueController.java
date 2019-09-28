package ua.edu.sumdu.nc.controllers.delete.issues;

import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
        try {
            Issue issue = appCtx.getBean("IssueSearcher", IssueSearcher.class)
                    .getIssueByID(request.getIssueId());
            if (issue != null) {
                issue.delete();
                logger.info("Issue (ID = " + request.getIssueId() + ") has been successfully deleted");
                return getCommonSuccessResponse(
                        "Issue (ID = " + request.getIssueId() + ") has been successfully deleted");
            }
            logger.error("Can not find issue. Request = (" + request.getIssueId() + ")");
            return getCommonErrorResponse("Can not find issue (ID = " + request.getIssueId() + ")");
        } catch (Exception e) {
            logger.error("Error while issue deletion (ID = " + request.getIssueId() + ")", e);
            return getCommonErrorResponse("Error while issue deletion (ID = " + request.getIssueId() + ")");
        }
    }

    @RequestMapping(
            path = "/delete/issue",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public Object delegateMethod(@RequestBody DeleteIssueRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
