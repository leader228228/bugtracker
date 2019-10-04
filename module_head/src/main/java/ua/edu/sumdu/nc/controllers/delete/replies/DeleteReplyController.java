package ua.edu.sumdu.nc.controllers.delete.replies;

import entities.bt.Reply;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.searchers.replies.ReplySearcher;
import ua.edu.sumdu.nc.validation.delete.replies.DeleteReplyRequest;

import javax.validation.Valid;

@RestController
public class DeleteReplyController extends Controller<DeleteReplyRequest> {
    public DeleteReplyController(@Qualifier(value = "appConfig")ApplicationContext appCtx) {
      super(appCtx);
    }

    @Override
    public Object handle(DeleteReplyRequest request) {
        try {
            return deleteReply(request.getReplyId());
        } catch (Exception e) {
            logger.error("Error during reply deletion (id = " + request.getReplyId() + ")", e);
            return getCommonErrorResponse("Error during reply deletion (id = " + request.getReplyId() + ")");
        }
    }

    @RequestMapping(
        path = "/delete/reply",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object proxyMethod(@Valid @RequestBody DeleteReplyRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request);
        return getInvalidRequestResponse(bindingResult);
    }

    private Object deleteReply(long replyId) throws Exception {
        ReplySearcher replySearcher = appCtx.getBean("ReplySearcher", ReplySearcher.class);
        Reply reply = replySearcher.getReplyByID(replyId);
        if (reply != null) {
            reply.delete();
            logger.info("Reply (id = " + replyId + ") has been successfully deleted");
            return getCommonSuccessResponse(
                "Reply (id = " + replyId + ") has been successfully deleted");
        }
        logger.error("Can not find reply (id = " + replyId + ")");
        return getCommonErrorResponse("Can not find reply (id = " + replyId + ")");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/delete/reply/{id}", produces = "application/json")
    public Object proxyMethod(@PathVariable(value = "id") long replyId) {
        try {
            return deleteReply(replyId);
        } catch (Exception e) {
            logger.error("Unknown error while deleting the project (id = " + replyId + ")", e);
            return getCommonErrorResponse(
                "Unknown error while deleting the project (id = " + replyId + ")");
        }
    }
}
