package ua.edu.sumdu.nc.controllers.search;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.db.filters.Filter;
import ua.edu.sumdu.nc.db.filters.factory.FilterFactory;
import ua.edu.sumdu.nc.controllers.Controller;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class SearchController extends Controller {
    @Resource(name = "FilterFactory")
    private FilterFactory filterFactory;

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Object handle(@RequestBody String requestBody) {
        if (!isRequestBodyValid(requestBody)) {
            return INVALID_RESPONSE;
        }
        JSONObject requestBodyJSON = new JSONObject(requestBody);
        Filter filter = filterFactory.getFor(requestBodyJSON);
        Collection collection;
        try {
            collection = filter.execute();
        } catch (SQLException e) {
            return "Database error occurred";
        }
        return collection;
    }
}
