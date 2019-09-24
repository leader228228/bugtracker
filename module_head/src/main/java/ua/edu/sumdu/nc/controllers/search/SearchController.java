package ua.edu.sumdu.nc.controllers.search;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;

@RestController
public class SearchController extends Controller {

    public SearchController() {
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Object handle(/*@RequestBody*/ String requestBody) {
        // TODO
        return null;
    }
}
