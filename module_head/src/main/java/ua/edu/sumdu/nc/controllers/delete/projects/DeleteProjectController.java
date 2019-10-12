package ua.edu.sumdu.nc.controllers.delete.projects;

import dao.DAO;
import entities.bt.Project;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.validation.delete.projects.DeleteProjectRequest;

import javax.validation.Valid;

@RestController
public class DeleteProjectController extends Controller<DeleteProjectRequest> {

    public DeleteProjectController(@Qualifier(value = "appConfig") ApplicationContext appCtx, DAO DAO, Utils utils) {
        super(appCtx, DAO, utils);
    }

    @Override
    public Object handle(DeleteProjectRequest request) {
        try {
            return deleteProject(request.getProjectId());
        } catch (Exception e) {
            logger.error("Unknown error while deleting the project. Request = (" + request + ")", e);
            return getCommonErrorResponse(
                "Unknown error while deleting the project (id = " + request.getProjectId() + ")");
        }
    }

    private Project findProjectById(long projectId) {
        return utils.getProjectSearcher().getProjectByID(projectId);
    }

    private Object deleteProject(long projectId) throws Exception {
        Project project = findProjectById(projectId);
        if (project != null) {
            project.delete();
            logger.info(
                "Project (id = "
                    + projectId
                    + ") has been successfully deleted");
            return getCommonSuccessResponse(
                "Project (id = " + projectId + ") has been successfully deleted");
        }
        logger.error("Unable to find project (id = " + projectId + ")");
        return getCommonErrorResponse("Unable to find project (id = " + projectId + ")");
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

    @RequestMapping(method = RequestMethod.GET, path = "/delete/project/{id}", produces = "application/json")
    public Object proxyMethod(@PathVariable(value = "id") long projectId) {
        try {
            return deleteProject(projectId);
        } catch (Exception e) {
            logger.error("Unknown error while deleting the project (id = " + projectId + ")", e);
            return getCommonErrorResponse(
                "Unknown error while deleting the project (id = " + projectId + ")");
        }
    }
}
