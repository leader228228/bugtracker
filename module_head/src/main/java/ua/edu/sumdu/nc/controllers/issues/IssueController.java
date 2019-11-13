package ua.edu.sumdu.nc.controllers.issues;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.bt.Issue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.services.IssueService;
import ua.edu.sumdu.nc.controllers.Utils;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;
import ua.edu.sumdu.nc.validation.update.issues.UpdateIssueRequest;

import java.sql.SQLException;
import java.util.Collection;

@Validated
@RestController(value = "/issues")
public class IssueController {

    private Logger logger = Logger.getRootLogger();
    @Autowired
    private IssueService issueService;

    @RequestMapping(
        path = "/create",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public ResponseEntity<String> createIssue(@RequestBody CreateIssueRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Invalid request: " + request.toString());
            return new ResponseEntity<>(Utils.getInvalidRequestResponse(bindingResult), HttpStatus.BAD_REQUEST);
        }
        Issue issue;
        try {
            issue = issueService.createIssue(request);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse("Unknown error occurred", e.toString()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return new ResponseEntity<>(Utils.getCommonSuccessResponse(
            "The issue has been successfully created", "issue_id = " + issue.getIssueID()), HttpStatus.OK
        );
    }

    @RequestMapping(
        path = "/delete/{issue_id}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> deleteIssue(@PathVariable(name = "issue_id") long issueID) {
        try {
            issueService.deleteIssue(issueID);
        } catch (SQLException e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Utils.getCommonSuccessResponse(
            "The issue id = " + issueID + " was deleted (if it existed)"), HttpStatus.ACCEPTED
        );
    }

    @RequestMapping(
        path = "/update",
        method = RequestMethod.POST,
        produces = "application/json",
        consumes = "application/json"
    )
    public ResponseEntity<String> updateIssue(@RequestBody UpdateIssueRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Invalid request: " + request.toString());
            return new ResponseEntity<>(Utils.getInvalidRequestResponse(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            issueService.updateIssue(request);
        } catch (SQLException e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Utils.getCommonSuccessResponse(
            "The issue id = " + request.getIssueID() + " was successfully updated (if it existed)"),
            HttpStatus.ACCEPTED
        );
    }

    /**
     * @param           issueIDs a set of {@code issue_id}s of the issues are requested
     *                           if no one issue is passed, all the issues are returned
     * */
    @RequestMapping(
        path = "/search/id/{issue_ids}",
        method = RequestMethod.GET,
        produces = "application/json",
        consumes = "application/json"
    )
    public ResponseEntity<String> searchIssuesByIDs(@PathVariable(name = "issue_ids") long [] issueIDs) {
        if (issueIDs == null) {
            logger.error("issueIDs is null");
            return new ResponseEntity<>(Utils.getCommonErrorResponse("Internal error"),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        try {
            return new ResponseEntity<>(buildForResponse(issueService.getIssues(issueIDs)), HttpStatus.OK);
        } catch (SQLException e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private String buildForResponse(Collection<Issue> issues) {
        try {
            return new ObjectMapper().writer().writeValueAsString(new Object() {
                public Collection<Issue> _issues = issues;
            });
        } catch (JsonProcessingException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(
        path = "/search/text/{text}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> searchIssuesByText(@PathVariable(name = "text") String text) {
        try {
            return new ResponseEntity<>(buildForResponse(issueService.getIssues(text)), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(
        path = "/search/reporter/{reporter_ids}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> searchIssuesByReporters(@PathVariable(name = "reporter_ids") long [] reporterIDs) {
        try {
            return new ResponseEntity<>(buildForResponse(issueService.getIssuesByReporters(reporterIDs)), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(
        path = "/search/assignee/{assignee_ids}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> searchIssuesByAssignees(@PathVariable(name = "assignee_ids") long [] assigneeIDs) {
        try {
            return new ResponseEntity<>(buildForResponse(issueService.getIssuesByAssignees(assigneeIDs)), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
