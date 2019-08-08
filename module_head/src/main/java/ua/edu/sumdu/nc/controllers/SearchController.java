package ua.edu.sumdu.nc.controllers;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import filters.Filter;
import filters.factory.FilterFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
            return INVALID_REQUEST;
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
