package ua.edu.sumdu.nc.controllers.issues;

import entities.bt.Issue;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.controllers.Utils;
import services.issues.IssueService;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;
import ua.edu.sumdu.nc.validation.update.issues.UpdateIssueRequest;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Validated
@RestController(value = "/issues")
public class IssueController {

    private Logger logger = Logger.getRootLogger();

    private IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

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
            issue = issueService.createIssue(
                request.getStatusID(),
                request.getProjectID(),
                request.getTitle(),
                request.getBody(),
                request.getAssigneeID(),
                request.getReporterID()
            );
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
    public ResponseEntity<String> updateIssue(@Valid @RequestBody UpdateIssueRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Invalid request: " + request.toString());
            return new ResponseEntity<>(Utils.getInvalidRequestResponse(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            issueService.updateIssue(
                request.getAssigneeID(),
                request.getStatusID(),
                request.getProjectID(),
                request.getBody(),
                request.getTitle(),
                request.getIssueID()
            );
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
     * @param           issuesIDs a set of {@code issue_id}s of the issues are requested
     *                           if no one issue is passed, all the issues are returned
     * */
    @RequestMapping(
        path = "/search/id/{issues_ids}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> searchIssuesByIDs(@PathVariable(name = "issues_ids") String [] issuesIDs) {
        Collection<Long> _issuesIDs;
        try {
            _issuesIDs = Arrays.stream(issuesIDs).map(Long::valueOf).collect(Collectors.toCollection(HashSet::new));
        } catch (NumberFormatException e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse("Wrong number format"),
                HttpStatus.BAD_REQUEST
            );
        }
        try {
            return new ResponseEntity<>(Utils.buildForResponse(issueService.getIssues(_issuesIDs)), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(
        path = "/search/text/{text}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> searchIssuesByText(@PathVariable(name = "text") String text) {
        try {
            return new ResponseEntity<>(Utils.buildForResponse(issueService.getIssues(text)), HttpStatus.OK);
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
    public ResponseEntity<String> searchIssuesByReporters(@PathVariable(name = "reporter_ids") String [] reporterIDs) {
        Collection<Long> _reporterIDs;
        try {
            _reporterIDs = Arrays.stream(reporterIDs).map(Long::valueOf).collect(Collectors.toCollection(HashSet::new));
        } catch (NumberFormatException e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse("Wrong number format"),
                HttpStatus.BAD_REQUEST
            );
        }
        try {
            return new ResponseEntity<>(Utils.buildForResponse(issueService.getIssuesByReporters(_reporterIDs)), HttpStatus.OK);
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
    public ResponseEntity<String> searchIssuesByAssignees(@PathVariable(name = "assignee_ids") String [] assigneeIDs) {
        Collection<Long> _assigneeIDs;
        try {
            _assigneeIDs = Arrays.stream(assigneeIDs).map(Long::valueOf).collect(Collectors.toCollection(HashSet::new));
        } catch (NumberFormatException e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse("Wrong number format"),
                HttpStatus.BAD_REQUEST
            );
        }
        try {
            return new ResponseEntity<>(
                Utils.buildForResponse(issueService.getIssuesByAssignees(_assigneeIDs)),
                HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
