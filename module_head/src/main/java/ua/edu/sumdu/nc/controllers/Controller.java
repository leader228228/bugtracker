package ua.edu.sumdu.nc.controllers;

import dao.DAO;
import entities.bt.PersistanceEntity;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class Controller {
    protected ApplicationContext applicationContext;
    protected dao.DAO DAO;
    protected Object response;
    protected static final String ERROR_RESPONSE_TEMPLATE = "{\"status\": \"error\",\"message\":\"#message#\"}";
    protected static final String SUCCESS_RESPONSE_TEMPLATE = "{\"status\": \"success\",\"message\":\"#message#\"}";
    protected static final Logger logger = Logger.getRootLogger();
    protected Schema schema;

    public Controller(
            @Autowired Schema schema,
            @Autowired DAO DAO,
            @Autowired ApplicationContext applicationContext) {
        this.schema = schema;
        this.DAO = DAO;
        this.applicationContext = applicationContext;
    }

    protected boolean isRequestBodyValid(Object requestBody) {
        try {
            schema.validate(requestBody);
        } catch (ValidationException /*| JSONException*/ e) {
            //logger.error("Invalid request", e);
            /*e.getCausingExceptions().forEach(e1 -> { // debug
                throw new RuntimeException("Schema location(" + e1.getViolatedSchema().toString()+ ")");
            });*/
            StringBuilder stringBuilder = new StringBuilder();
            e.getAllMessages().forEach(stringBuilder::append);
            stringBuilder.append(",Request itself(").append(e.getViolatedSchema()).append(")");
            throw new RuntimeException(stringBuilder.toString(),e);
            //return false;
        }
        return true;
    }

    protected String getRequestBodyAsString(HttpServletRequest httpServletRequest) throws IOException {
        return IOUtils.toString(httpServletRequest.getInputStream());
    }

    protected JSONObject getRequest(HttpServletRequest httpServletRequest) throws IOException {
        return new JSONObject(getRequestBodyAsString(httpServletRequest));
    }

    protected String getErrorResponseMessage(String errorMessage) {
        return ERROR_RESPONSE_TEMPLATE.replaceFirst("#message#", errorMessage);
    }

    protected String getSuccessResponseMessage(String successMessage) {
        return SUCCESS_RESPONSE_TEMPLATE.replaceFirst("#message#", successMessage);
    }
}
