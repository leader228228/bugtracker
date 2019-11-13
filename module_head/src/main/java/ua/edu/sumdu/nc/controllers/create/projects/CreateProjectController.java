package ua.edu.sumdu.nc.controllers.create.projects;

import dao.DAO;
import entities.bt.Project;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Utils;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.validation.create.projects.CreateProjectRequest;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
public class CreateProjectController extends Controller<CreateProjectRequest> {

    public CreateProjectController(@Qualifier(value = "appConfig") ApplicationContext appCtx, DAO DAO, Utils utils) {
        super(appCtx, DAO, utils);
    }

    @Override
    public Object handle(CreateProjectRequest request) {
        Project project = utils.getProject();
        project.setName(request.getName());
        try {
            project.setProjectID(DAO.getId());
            project.save();
        } catch (SQLException e) {
            logger.error("Unknown error while saving the project, request=(" + request + ")", e);
            return getCommonErrorResponse("Error due to access to database: ", e.getClass().toString());
        }
        return getCommonSuccessResponse("The project id = " + project.getProjectID() + " has been created");
    }

    @RequestMapping(
        path = "/create/project",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object delegateMethod(@Valid @RequestBody CreateProjectRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
