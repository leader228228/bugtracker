package ua.edu.sumdu.nc.controllers.search;

import org.everit.json.schema.Schema;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.controllers.Controller;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SearchController extends Controller {

    public SearchController(Schema schema) {
        super(schema);
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Object handle(HttpServletRequest httpServletRequest) {
        // TODO
        return null;
    }
}
