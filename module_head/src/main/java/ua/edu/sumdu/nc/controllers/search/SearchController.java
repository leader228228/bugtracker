package ua.edu.sumdu.nc.controllers.search;

import org.json.JSONObject;
import ua.edu.sumdu.nc.controllers.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
