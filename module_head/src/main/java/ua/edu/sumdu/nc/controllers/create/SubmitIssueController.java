package ua.edu.sumdu.nc.controllers.create;

import entities.bt.Issue;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.controllers.create.parsers.InputParser;

@RestController
public class SubmitIssueController extends Controller {
    private InputParser<JSONObject, Issue> parser;
    private ApplicationContext applicationContext;
    private Object response;
    private Issue issue;
    public SubmitIssueController(
            @Autowired ApplicationContext applicationContext,
            @Autowired @Qualifier(value = "IssueParser") InputParser<JSONObject, Issue> parser) {
        this.applicationContext = applicationContext;
        this.parser = parser;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/create")
    @ResponseBody
    public Object handle(@RequestBody String requestBody) throws Exception {
        if (!isRequestBodyValid(requestBody)) {
            return INVALID_RESPONSE;
        }
        issue = parser.parse(new JSONObject(requestBody));
        issue.updateOrSave();
        return response;
    }

    private void prepareRsponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operations", "creation").put("status", 200).put("issue", issue);
        response = jsonObject;
    }
}
