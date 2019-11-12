package ua.edu.sumdu.nc.controllers.create.users;

import entities.bt.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Utils;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.validation.create.users.CreateUserRequest;

import javax.validation.Valid;

@RestController
public class CreateUserController extends Controller<CreateUserRequest> {

    public CreateUserController(@Qualifier(value = "appConfig") ApplicationContext appCtx, dao.DAO DAO, Utils utils) {
        super(appCtx, DAO, utils);
    }

    @Override
    public Object handle(CreateUserRequest request) {
        User user = utils.getUser();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        try {
            user.setUserId(DAO.getId());
            user.save();
        } catch (Exception e) {
            logger.error("Unknown error while saving new user, request=(" + request + ")", e);
            return getCommonErrorResponse("Error due to access to database: ", e.getClass().toString());
        }
        return getCommonSuccessResponse("The user " + user.getUserId() + " has been created");
    }

    @RequestMapping(
        path = "/create/user",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object proxyMethod(@RequestBody @Valid CreateUserRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
