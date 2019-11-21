package ua.edu.sumdu.nc.controllers.replies;

import entities.bt.Reply;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import services.replies.ReplyService;
import ua.edu.sumdu.nc.controllers.Utils;
import ua.edu.sumdu.nc.validation.create.replies.CreateReplyRequest;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RestController(value = "/reply")
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
            Reply reply = replyService.createReply(
                request.getAuthorID(),
                request.getBody(),
                request.getIssueID()
            );
            return new ResponseEntity<>(
                Utils.getCommonSuccessResponse("Reply has been successfully created",
                "reply_id = " + reply.getReplyID()), HttpStatus.OK
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
            replyService.deleteReplies(Arrays.stream(replyID).boxed().collect(Collectors.toSet()));
            return new ResponseEntity<>(
                Utils.getCommonSuccessResponse("The replies have been successfully deleted (existed)"),
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
