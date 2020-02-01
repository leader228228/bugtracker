package ua.edu.sumdu.nc.controllers;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.entities.Project;
import ua.edu.sumdu.nc.services.ProjectService;
import ua.edu.sumdu.nc.validation.projects.CreateProjectRequest;

@RestController
@RequestMapping(path = "/projects")
public class ProjectController {

    private final ProjectService projectService;

    private final Logger logger = Logger.getRootLogger();

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(
        path = "/create",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public ResponseEntity<String> createProject(@RequestBody CreateProjectRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Invalid request: " + request.toString());
            return new ResponseEntity<>(Utils.getInvalidRequestResponse(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            Project project = projectService.createProject(request.getName());
            return new ResponseEntity<>(
                Utils.getCommonSuccessResponse(project.toString()),
                HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(Utils.getCommonErrorResponse("Unknown error occurred", e.toString()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(
        path = "/search/id/{project_ids}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> searchProject(@PathVariable(name = "project_ids") long [] projectIDs) {
        try {
            if (projectIDs.length == 0) {
                return new ResponseEntity<>(Utils.buildForResponse(projectService.getAll()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                    Utils.buildForResponse(projectService.searchProjectsByIDs(projectIDs)),
                    HttpStatus.OK
                );
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(
                Utils.getCommonErrorResponse(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(
        path = "/search/name/{project_ids}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> searchProject(@PathVariable(name = "project_ids") String projectName) {
        try {
            if(projectName.isEmpty()) {
                return new ResponseEntity<>(Utils.buildForResponse(projectService.getAll()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Utils.buildForResponse(projectService.searchProjectsByName(projectName)),
                    HttpStatus.OK
                );
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(
                Utils.getCommonErrorResponse(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @RequestMapping(
        path = "/delete/{project_id}",
        method = RequestMethod.GET,
        produces = "application/json"
    )
    public ResponseEntity<String> searchProject(@PathVariable(name = "project_id") long projectID) {
        try {
            projectService.deleteProject(projectID);
            return new ResponseEntity<>(
                Utils.getCommonSuccessResponse(
                    "The project and all its issues have been successfully deleted (if the project existed)"),
                HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(
                Utils.getCommonErrorResponse(e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
