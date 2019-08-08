package ua.edu.sumdu.nc.controllers;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;

@RestController
public class SubmitIssueController extends Controller {
    @RequestMapping
    @ResponseBody
    public Object handle(@RequestBody String requestBody) {
        if (!isRequestBodyValid(requestBody)) {
            return INVALID_REQUEST;
        }

    }

}
