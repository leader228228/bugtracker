package ua.edu.sumdu.nc.controllers.create.issues;

import dao.DAO;
import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;

import javax.validation.Valid;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
public class CreateIssueController extends Controller<CreateIssueRequest> {

    private final ApplicationContext appCtx;

    public CreateIssueController(@Qualifier(value = "appConfig") ApplicationContext appCtx, @Autowired DAO DAO) {
        super(DAO);
        this.appCtx = appCtx;
    }

    @Override
    public Object handle(CreateIssueRequest request) {
        Issue issue = appCtx.getBean( "Issue", Issue.class);
        issue.setStatusId(request.getStatusId());
        try {
            issue.setIssueId(DAO.getId());
        } catch (SQLException e) {
            logger.error("Unknown error while setting a new id to issue, request=(" + request + ")", e);
            return getCommonErrorResponse("Error due to access to database");
        }
        issue.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        issue.setTitle(request.getTitle());
        issue.setAssigneeId(request.getAssigneeId());
        issue.setBody(request.getBody());
        issue.setProjectId(request.getProjectId());
        issue.setReporterId(request.getReporterId());
        try {
            issue.save();
        } catch (SQLException e) {
            logger.error("Unknown error while saving an issue, request=(" + request + ")", e);
            return getCommonErrorResponse("Error due to access to database");
        }
        return getCommonSuccessResponse("Issue has been created", issue.toString());
    }

    @RequestMapping(
            path = "/create/issue",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json"
    )
    public Object delegateMethod(@Valid @RequestBody CreateIssueRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        return getInvalidInputResponse(bindingResult);
        //return getCommonErrorResponse(request.toString()); // debug
    }
}
