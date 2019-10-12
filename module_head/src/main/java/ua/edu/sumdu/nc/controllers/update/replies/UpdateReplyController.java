package ua.edu.sumdu.nc.controllers.update.replies;

import dao.DAO;
import entities.bt.Reply;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.searchers.replies.ReplySearcher;
import ua.edu.sumdu.nc.validation.update.replies.UpdateReplyRequest;

import javax.validation.Valid;

@RestController
public class UpdateReplyController extends Controller<UpdateReplyRequest> {

    public UpdateReplyController(@Qualifier(value = "appConfig") ApplicationContext appCtx, dao.DAO DAO, Utils utils) {
        super(appCtx, DAO, utils);
    }

    @Override
    public Object handle(UpdateReplyRequest request) {
        Reply reply = utils.getReplySearcher().getReplyByID(request.getReplyId());
        if (request.getBody() != null) {
            reply.setBody(request.getBody());
            logger.info("New body for the reply (reply_id = "
                + request.getReplyId() + ") = " + request.getBody());
        }
        try {
            reply.update();
            logger.info("The reply (reply_id = " + request.getReplyId() + ") has been successfully updated");
            return getCommonSuccessResponse(
                "The reply (reply_id = " + request.getReplyId() + ") has been successfully updated");
        } catch (Exception e) {
            logger.error("The reply (reply_id = " + request.getReplyId() + ") updating was interrupted", e);
            return getCommonErrorResponse(
                "The issue (issue_id = " + request.getReplyId() + ") updating was interrupted", e.getMessage());
        }
    }

    @RequestMapping(
        path = "/update/reply",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object proxyMethod(@Valid @RequestBody UpdateReplyRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
