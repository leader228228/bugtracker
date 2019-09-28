package ua.edu.sumdu.nc.controllers.delete.users;

import entities.bt.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.searchers.users.UserSearcher;
import ua.edu.sumdu.nc.validation.delete.users.DeleteUserRequest;

import javax.validation.Valid;

@RestController
public class DeleteUserController extends Controller<DeleteUserRequest> {

    public DeleteUserController(@Qualifier(value = "appConfig") ApplicationContext appCtx) {
        super(appCtx);
    }

    @Override
    public Object handle(DeleteUserRequest request) {
        UserSearcher replySearcher = appCtx.getBean("UserSearcher", UserSearcher.class);
        User user = replySearcher.getUserByID(request.getUserId());
        try {
            if (user != null) {
                user.delete();
                logger.info("User (ID = " + request.getUserId() + ") has been successfully deleted. Request = (" + request + ")");
                return getCommonSuccessResponse(
                    "User (ID = " + request.getUserId() + ") has been successfully deleted");
            }
            logger.error("Can not find user (ID = " + request.getUserId() + ")");
            return getCommonErrorResponse("Can not find user (ID = " + request.getUserId() + ")");
        } catch (Exception e) {
            logger.error("Error during user deletion. Request = (" + request + ")", e);
            return getCommonErrorResponse("Error during user deletion (ID = " + request.getUserId() + ")");
        }
    }

    @RequestMapping(
        path = "/delete/user",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object proxyMethod(@RequestBody @Valid DeleteUserRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
