package ua.edu.sumdu.nc.controllers.delete.projects;

import entities.bt.Issue;
import entities.bt.Project;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.searchers.projects.ProjectSearcher;
import ua.edu.sumdu.nc.validation.create.projects.CreateProjectRequest;
import ua.edu.sumdu.nc.validation.delete.projects.DeleteProjectRequest;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
public class DeleteProjectController extends Controller<DeleteProjectRequest> {

    public DeleteProjectController(@Qualifier(value = "appConfig") ApplicationContext appCtx) {
        super(appCtx);
    }

    @Override
    public Object handle(DeleteProjectRequest request) {
        Project project = appCtx.getBean("ProjectSearcher", ProjectSearcher.class)
            .getProjectByID(request.getProjectId());
        if (project != null) {
            try {
                project.delete();
                logger.info(
                    "Project (ID = "
                    + request.getProjectId()
                    + ") has been successfully deleted. Request = ("
                    + request + ")");
                return getCommonSuccessResponse(
                    "Project (ID = " + request.getProjectId() + ") has been successfully deleted");
            } catch (Exception e) {
                logger.error("Unknown error while deleting the project. Request = (" + request + ")", e);
                return getCommonErrorResponse(
                    "Unknown error while deleting the project (ID = " + request.getProjectId() + ")");
            }
        }
        logger.error("Unable to find project. Request = (" + request + ")");
        return getCommonErrorResponse("Unable to find project (ID = " + request.getProjectId() + ")");
    }

    @RequestMapping(
        path = "/delete/project",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )

    public Object delegateMethod(@Valid @RequestBody DeleteProjectRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
