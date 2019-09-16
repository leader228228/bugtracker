package ua.edu.sumdu.nc.controllers;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.Resource;

public abstract class Controller extends DispatcherServlet {

    public static final JSONObject INVALID_RESPONSE = new JSONObject("{\"error\":\"invalid request\"}");

    @Resource(name = "BTRequestSchema")
    protected Schema schema;

    protected boolean isRequestBodyValid(JSONObject requestBody) {
        try {
            schema.validate(requestBody);
        } catch (ValidationException | JSONException e) {
            return false;
        }
        return true;
    }

    protected boolean isRequestBodyValid(String requestBody) {
        JSONObject jsonObject = new JSONObject(requestBody);
        return isRequestBodyValid(jsonObject);
    }
}
