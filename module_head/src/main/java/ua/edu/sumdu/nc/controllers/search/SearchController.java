package ua.edu.sumdu.nc.controllers.search;

import ua.edu.sumdu.nc.controllers.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.db.filters.FilterSelector;

@RestController
public class SearchController extends Controller {
    private FilterSelector filterSelector;

    public SearchController(@Autowired FilterSelector filterSelector) {
        this.filterSelector = filterSelector;
    }

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Object handle(/*@RequestBody*/ String requestBody) {
        return "It works";
        /*if (!isRequestBodyValid(requestBody)) {
            return INVALID_RESPONSE;
        }
        JSONObject requestBodyJSON = new JSONObject(requestBody);
        Filter filter;
        Collection collection;
        try {
            filter = filterSelector.filterForRequest(requestBodyJSON);
            collection = filter.execute();
        } catch (Exception e) {
            return new JSONObject().put("error", e.getClass()); // ?
        }
        return new JSONObject().put("response", collection);*/
    }
}
