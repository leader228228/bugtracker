package ua.edu.sumdu.nc.controllers;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.entities.Reply;
import ua.edu.sumdu.nc.services.ReplyService;
import ua.edu.sumdu.nc.validation.replies.CreateReplyRequest;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/reply")
public class ReplyController {

    private final Logger logger = Logger.getRootLogger();
    private ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @RequestMapping(
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json",
        path = "/create"
    )
    public ResponseEntity<String> createReply(@Valid @RequestBody CreateReplyRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(Utils.getCommonErrorResponse("Bad request"), HttpStatus.BAD_REQUEST);
        }
        try {
            Reply reply = replyService.createReply(request.getAuthorID(), request.getBody(), request.getIssueID());
            return new ResponseEntity<>(
                Utils.getCommonSuccessResponse(reply.toString()), HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(
                Utils.getCommonErrorResponse("Internal error"),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(
        method = RequestMethod.GET,
        produces = "application/json",
        path = "/delete/{reply_ids}"
    )
    public ResponseEntity<String> deleteReply(@PathVariable(name = "reply_ids") long [] replyID) {
        try {
            boolean allRepliesHaveBeenDeleted =
                    replyService.deleteReplies(Arrays.stream(replyID).boxed().collect(Collectors.toList()));
            if (allRepliesHaveBeenDeleted) {
                return new ResponseEntity<>(
                        Utils.getCommonSuccessResponse(
                                "All the replies have been successfully deleted"
                        ),
                        HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                        Utils.getCommonSuccessResponse(
                                "Not all the replies have been successfully deleted"
                        ),
                        HttpStatus.ACCEPTED
                );
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(
                Utils.getCommonErrorResponse("Internal error"),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(
        method = RequestMethod.GET,
        produces = "application/json",
        path = "/search/author/{authors_ids}"
    )
    public ResponseEntity<String> searchRepliesByAuthorsIDs(@PathVariable(name = "authors_ids") long [] authorIDs) {
        try {
            return new ResponseEntity<>(
                Utils.getCommonSuccessResponse(Utils.buildForResponse(replyService.searchRepliesByAuthorsIDs(authorIDs))),
                HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(
                Utils.getCommonErrorResponse("Internal error"),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(
        method = RequestMethod.GET,
        produces = "application/json",
        path = "/search/text/{text}"
    )
    public ResponseEntity<String> searchRepliesByAuthorsIDs(@PathVariable(name = "text") String text) {
        try {
            return new ResponseEntity<>(
                Utils.getCommonSuccessResponse(Utils.buildForResponse(replyService.searchRepliesByText(text))),
                HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(
                Utils.getCommonErrorResponse("Internal error"),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(
        method = RequestMethod.GET,
        produces = "application/json",
        path = "/search/issue/{issues_ids}"
    )
    public ResponseEntity<String> searchRepliesByIssuesIDs(@PathVariable(name = "issues_ids") long [] issuesIDs) {
        try {
            return new ResponseEntity<>(
                Utils.getCommonSuccessResponse(Utils.buildForResponse(replyService.searchRepliesByIssuesIDs(issuesIDs))),
                HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(
                Utils.getCommonErrorResponse("Internal error"),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
