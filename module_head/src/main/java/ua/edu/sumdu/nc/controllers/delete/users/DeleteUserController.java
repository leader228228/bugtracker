package ua.edu.sumdu.nc.controllers.delete.users;

import entities.bt.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.controllers.Utils;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.searchers.users.UserSearcher;
import ua.edu.sumdu.nc.validation.delete.users.DeleteUserRequest;

import javax.validation.Valid;

@RestController
public class DeleteUserController extends Controller<DeleteUserRequest> {

    public DeleteUserController(@Qualifier(value = "appConfig")ApplicationContext appCtx, dao.DAO DAO, Utils utils) {
        super(appCtx, DAO, utils);
    }

    @Override
    public Object handle(DeleteUserRequest request) {
        try {
            return deleteUser(request.getUserId());
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

    private Object deleteUser(long userId) throws Exception {
        UserSearcher replySearcher = utils.getUserSearcher();
        User user = replySearcher.getUserByID(userId);
        if (user != null) {
            user.delete();
            logger.info("User (id = " + userId + ") has been successfully deleted");
            return getCommonSuccessResponse(
                "User (id = " + userId + ") has been successfully deleted");
        }
        logger.error("Can not find user (id = " + userId + ")");
        return getCommonErrorResponse("Can not find user (id = " + userId + ")");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/delete/user/{id}", produces = "application/json")
    public Object proxyMethod(@PathVariable(value = "id") long userId) {
        try {
            return deleteUser(userId);
        } catch (Exception e) {
            logger.error("Unknown error while deleting the user (id = " + userId + ")", e);
            return getCommonErrorResponse(
                "Unknown error while deleting the user (id = " + userId + ")");
        }
    }
}
