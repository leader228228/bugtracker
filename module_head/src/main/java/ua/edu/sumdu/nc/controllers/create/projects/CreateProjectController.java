package ua.edu.sumdu.nc.controllers.create.projects;

import dao.DAO;
import entities.bt.Project;
import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class CreateProjectController extends Controller {

    public CreateProjectController(
            @Autowired Schema schema,
            @Autowired DAO DAO,
            @Qualifier("appConfig") ApplicationContext applicationContext) {
        super(schema, DAO, applicationContext);
    }

    @RequestMapping(value = "/create/project", method = RequestMethod.POST)
    public Object handle(HttpServletRequest httpServletRequest) {
        Project project;
        JSONObject request;
        try {
            request = getRequest(httpServletRequest);
        } catch (IOException e) {
            logger.error("Error during request body reading", e);
            return getErrorResponseMessage("Internal error");
        }
        if (!isRequestBodyValid(request)) {
            logger.error("Invalid request body : " + request);
            return getErrorResponseMessage("Invalid request body");
        }
        project = applicationContext.getBean("Project", Project.class);
        project.setName(request.getString("projectName"));
        try {
            project.setProjectId(DAO.getId());
            project.save();
        } catch (Exception e) {
            logger.error("Error during saving new project", e);
            return getErrorResponseMessage("Internal error");
        }
        return getSuccessResponseMessage("Project has been successfully created");
    }
}
