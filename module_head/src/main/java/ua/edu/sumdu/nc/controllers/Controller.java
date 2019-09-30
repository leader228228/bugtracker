package ua.edu.sumdu.nc.controllers;

import dao.DAO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ua.edu.sumdu.nc.validation.BTRequest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public abstract class Controller<T extends BTRequest> {
    protected static final Logger logger = Logger.getRootLogger();
    protected DAO DAO;
    protected ApplicationContext appCtx;

    public Controller(@Autowired ApplicationContext appCtx) {
        this.appCtx = appCtx;
        DAO = appCtx.getBean("DAO", DAO.class);
    }

    public abstract Object handle(T request);
    protected static final String RESP_JSON_TEMPL = "{\"status\":\"#status#\",\"messages\":[#messages#]}";

    protected static String getCommonErrorResponse(String...messages) {
        return RESP_JSON_TEMPL
                .replaceFirst("#status#", "error")
                .replaceFirst("#messages#", wrapAndJoin(messages));
    }

    protected static String getCommonSuccessResponse(String...messages) {
        return RESP_JSON_TEMPL
                .replaceFirst("#status#", "success")
                .replaceFirst("#messages#", wrapAndJoin(messages));
    }

    private static String wrapAndJoin(String...strings) {
        StringBuilder stringBuilder = new StringBuilder();
        if (strings.length != 0) {
            stringBuilder = new StringBuilder("\"");
            stringBuilder.append(String.join("\", \"", strings)).append("\"");
        }
        return stringBuilder.toString();
    }

    protected String getInvalidRequestResponse(BindingResult bindingResult) {
        String [] errorMessages = new String[]{};
        if (bindingResult.hasErrors()) {
            List<String> list = new ArrayList<>(bindingResult.getAllErrors().size());
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                list.add(objectError.getCode());
            }
            errorMessages = list.toArray(new String[0]);
        }
        return getCommonErrorResponse(errorMessages);
    }

    protected String arrayToString(int [] arr) {
        if (arr == null || arr.length == 0) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : arr) {
            stringBuilder.append(i).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    protected String arrayToString(long [] arr) {
        if (arr == null || arr.length == 0) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (long l : arr) {
            stringBuilder.append(l).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    protected String arrayToString(Timestamp[] arr) {
        if (arr == null || arr.length == 0) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Timestamp t : arr) {
            stringBuilder.append("'").append(t.toString()).append("'").append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
