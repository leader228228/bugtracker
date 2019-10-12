package ua.edu.sumdu.nc.controllers.create.replies;

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
import ua.edu.sumdu.nc.validation.create.replies.CreateReplyRequest;

import javax.validation.Valid;

@RestController
public class CreateReplyController extends Controller<CreateReplyRequest> {

    public CreateReplyController(@Qualifier(value = "appConfig") ApplicationContext appCtx, DAO DAO, Utils utils) {
        super(appCtx, DAO, utils);
    }

    @Override
    public Object handle(CreateReplyRequest request) {
        Reply reply = utils.getReply();
        reply.setAuthorId(request.getAuthorId());
        reply.setBody(request.getBody());
        reply.setIssueId(request.getIssueId());
        try {
            reply.setReplyId(DAO.getId());
            reply.save();
        } catch (Exception e) {
            logger.error("Unknown error while saving the reply, request=(" + request + ")", e);
            return getCommonErrorResponse("Error due to access to database: ", e.getClass().toString());
        }
        return getCommonSuccessResponse("The reply " + reply.getReplyId() + " has been successfully crated");
    }

    @RequestMapping(
        path = "/create/reply",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object proxyMethod(@Valid @RequestBody CreateReplyRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
