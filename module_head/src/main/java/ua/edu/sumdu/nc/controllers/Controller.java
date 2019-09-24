package ua.edu.sumdu.nc.controllers;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Controller {

    protected static final String ERROR_RESPONSE_TEMPLATE = "{\"status\": \"error\",\"message\":\"#message#\"}";
    protected static final String SUCCESS_RESPONSE_TEMPLATE = "{\"status\": \"success\",\"message\":\"#message#\"}";

    protected Schema schema;

    public Controller(@Autowired Schema schema) {
        this.schema = schema;
    }

    protected boolean isRequestBodyValid(Object requestBody) {
        try {
            schema.validate(requestBody);
        } catch (ValidationException | JSONException e) {
            /*e.getCausingExceptions().forEach(e1 -> { // debug
                throw new RuntimeException("Schema location(" + e1.getViolatedSchema().toString()+ ")");
            });
            StringBuilder stringBuilder = new StringBuilder();
            e.getAllMessages().forEach(stringBuilder::append);
            stringBuilder.append(",Request itself(").append(requestBody).append(")");
            throw new RuntimeException(stringBuilder.toString());*/
            return false;
        }
        return true;
    }
}
