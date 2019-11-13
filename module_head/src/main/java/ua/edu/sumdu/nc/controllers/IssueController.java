package ua.edu.sumdu.nc.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.IssueService;
import ua.edu.sumdu.nc.validation.create.issues.CreateIssueRequest;
import ua.edu.sumdu.nc.validation.delete.issues.DeleteIssueRequest;

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
    public Object createIssue(@RequestBody CreateIssueRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Invalid request: " + request.toString());
            return Utils.getInvalidRequestResponse(bindingResult);
        }
        try {
            issueService.createIssue(request);
        } catch (Exception e) {
            logger.error(e);
            return Utils.getCommonErrorResponse("Unknown error occurred", e.toString());
        }
        return Utils.getCommonSuccessResponse("The issue has been successfully created");
    }

    @RequestMapping(
        path = "/delete/{issue_id}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> deleteIssue(@PathVariable(name = "issue_id") long issueID) {
        issueService.deleteIssue(issueID);
    }
}
