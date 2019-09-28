package ua.edu.sumdu.nc.controllers.delete.replies;

import entities.bt.Reply;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.searchers.replies.ReplySearcher;
import ua.edu.sumdu.nc.validation.create.replies.CreateReplyRequest;
import ua.edu.sumdu.nc.validation.delete.replies.DeleteReplyRequest;

import javax.validation.Valid;

@RestController
public class DeleteReplyController extends Controller<DeleteReplyRequest> {
    public DeleteReplyController(@Qualifier(value = "appConfig")ApplicationContext appCtx) {
      super(appCtx);
    }

    @Override
    public Object handle(DeleteReplyRequest request) {
        ReplySearcher replySearcher = appCtx.getBean("ReplySearcher", ReplySearcher.class);
        Reply reply = replySearcher.getReplyByID(request.getReplyId());
        try {
            if (reply != null) {
                reply.delete();
                logger.info("Reply (ID = " + request.getReplyId() + ") has been successfully deleted. " +
                        "Request = (" + request + ")");
                return getCommonSuccessResponse(
                    "Reply (ID = " + request.getReplyId() + ") has been successfully deleted");
            }
            logger.error("Can not find reply (ID = " + request.getReplyId() + "). Request = (" + request + ")");
            return getCommonErrorResponse("Can not find reply (ID = " + request.getReplyId() + ")");
        } catch (Exception e) {
            logger.error("Error during reply deletion. Request = (" + request + ")", e);
            return getCommonErrorResponse("Error during reply deletion (ID = " + request.getReplyId() + ")");
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
}
