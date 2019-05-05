package ua.edu.sumdu.nc.controllers;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.data.filters.Filter;
import ua.edu.sumdu.nc.data.filters.factory.FilterFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class SearchController {
    @Resource(name = "BTRequestSchema")
    private static Schema schema;

    @Resource(name = "FilterFactory")
    private FilterFactory filterFactory;

    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Object handle(@RequestBody String requestBody, HttpServletResponse response) {
        try {
            if (!isRequestBodyValid(requestBody)) {
                response.setStatus(400);
                return "Invalid request";
            }
        } catch (JSONException e) {
            response.setStatus(400);
            return "JSON syntax exception";
        }
        JSONObject requestBodyJSON = new JSONObject(requestBody);
        Filter filter = filterFactory.getFor(requestBodyJSON);
        Collection collection;
        try {
            collection = filter.execute();
        } catch (SQLException e) {
            response.setStatus(500);
            return "Database error occurred";
        }
        return collection;
    }

    private boolean isRequestBodyValid(JSONObject requestBody) {
        try {
            schema.validate(requestBody);
        } catch (ValidationException e) {
            return false;
        }
        return true;
    }

    private boolean isRequestBodyValid(String requestBody) {
        JSONObject jsonObject = new JSONObject(requestBody);
        return isRequestBodyValid(jsonObject);
    }
}
