package ua.edu.sumdu.nc.controllers.create;

import entities.bt.Issue;
import ua.edu.sumdu.nc.controllers.Controller;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/create")
public class CreateIssueController extends Controller {
    private ApplicationContext applicationContext;
    private Object response;
    private Issue issue;
    public CreateIssueController(@Qualifier("appConfig") @Autowired ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/issue")
    @ResponseBody
    public Object handle(@RequestBody String requestBody) throws Exception {
        if (!isRequestBodyValid(requestBody)) {
            return INVALID_RESPONSE;
        }

        issue.updateOrSave();
        return response;
    }

}
