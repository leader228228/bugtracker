package ua.edu.sumdu.nc.controllers;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.entities.Issue;
import ua.edu.sumdu.nc.services.IssueService;
import ua.edu.sumdu.nc.validation.issues.CreateIssueRequest;
import ua.edu.sumdu.nc.validation.issues.UpdateIssueRequest;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/issues")
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
        return new ResponseEntity<>(Utils.getCommonSuccessResponse(issue.toString()), HttpStatus.OK);
    }

    @RequestMapping(
        path = "/delete/{issue_id}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> deleteIssue(@PathVariable(name = "issue_id") long issueID) {
        try {
            boolean hasIssueBeenDelete = issueService.deleteIssue(issueID);
            if (hasIssueBeenDelete) {
                return new ResponseEntity<>(
                        Utils.getCommonSuccessResponse("The issue has been successfully deleted"),
                        HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                        Utils.getCommonErrorResponse("The issue has not been found"),
                        HttpStatus.NOT_FOUND
                );
            }
        } catch (SQLException e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            boolean hasIssueBeenUpdated = issueService.updateIssue(
                request.getAssigneeID(),
                request.getStatusID(),
                request.getProjectID(),
                request.getBody(),
                request.getTitle(),
                request.getIssueID()
            );
            if (hasIssueBeenUpdated) {
                return new ResponseEntity<String>(
                        Utils.getCommonSuccessResponse(
                                issueService.getIssues(Collections.singletonList(request.getIssueID())).toString()
                        ),
                        HttpStatus.OK
                );
            } else {
                return new ResponseEntity<String>(
                        Utils.getCommonErrorResponse("The issue can not be updated"),
                        HttpStatus.BAD_REQUEST
                );
            }

        } catch (SQLException e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param           issueIDs a set of {@code issue_id}s of the issues are requested
     *                           if no one issue is passed, all the issues are returned
     * */
    @RequestMapping(
        path = "/search/id/{issue_ids}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> searchIssuesByIDs(@PathVariable(name = "issue_ids") long [] issueIDs) {
        if (issueIDs == null) {
            logger.error("issueIDs is null");
            return new ResponseEntity<>(Utils.getCommonErrorResponse("Internal error"),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        try {
            return new ResponseEntity<>(
                Utils.buildForResponse(
                    issueService.getIssues(Arrays.stream(issueIDs).boxed().collect(Collectors.toList()))
                ),
                HttpStatus.OK
            );
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
    public ResponseEntity<String> searchIssuesByReporters(@PathVariable(name = "reporter_ids") long [] reporterIDs) {
        try {
            return new ResponseEntity<>(
                Utils.buildForResponse(
                    issueService.getIssuesByReporters(Arrays.stream(reporterIDs).boxed().collect(Collectors.toList()))),
                HttpStatus.OK
            );
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
            return new ResponseEntity<>(
                Utils.buildForResponse(
                    issueService.getIssuesByAssignees(Arrays.stream(assigneeIDs).boxed().collect(Collectors.toList()))
                ),
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
