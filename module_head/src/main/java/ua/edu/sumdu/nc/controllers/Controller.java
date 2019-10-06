package ua.edu.sumdu.nc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dao.DAO;
import entities.bt.Entity;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.validation.BTRequest;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class Controller<T extends BTRequest> {
    protected static final Logger logger = Logger.getRootLogger();
    protected DAO DAO;
    protected ApplicationContext appCtx;
    protected Utils utils;
    protected char escapeChar = '&';

    public Controller(ApplicationContext appCtx) {
        this.appCtx = appCtx;
        DAO = appCtx.getBean("DAO", DAO.class);
        utils = appCtx.getBean("Utils", Utils.class);
    }

    protected Class<? extends Entity> getClassForMarshalling() {
        throw new UnsupportedOperationException();
    }

    protected String getPatternContains(String string) {
        return '%' + escapeRegexChars(string) + '%';
    }

    protected String escapeRegexChars(String string) {
        return string
            .replaceAll("%",escapeChar + "%")
            .replaceAll(String.valueOf(escapeChar), "" + escapeChar + escapeChar)
            .replaceAll("_", escapeChar + "_");
    }

    protected Collection<String> marshallEntitiesToJSON(Collection<? extends Entity> entities) throws IOException {
        List<String> result = new ArrayList<>(entities.size());
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerFor(getClassForMarshalling());
        StringWriter stringWriter = new StringWriter();
        for (Entity e : entities) {
            objectWriter.writeValue(stringWriter, e);
            result.add(stringWriter.toString());
            stringWriter = new StringWriter();
        }
        return result;
    }

    public abstract Object handle(T request);
    private static final String RESP_JSON_TEMPL = "{\"status\":\"#status#\",\"messages\":[#messages#]}";

    protected static String getCommonErrorResponse(String...messages) {
        return RESP_JSON_TEMPL
                .replaceFirst("#status#", "error")
                .replaceFirst("#messages#", wrapAndJoin(messages));
    }

    protected static String getCommonSuccessResponse(String...messages) {
        logger.debug("getCommonSuccessResponse " + String.join("[|||||]", messages));
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

    protected Entity readEntity(ResultSet resultSet) throws SQLException {
        throw new UnsupportedOperationException();
    }

    protected Collection<? extends Entity> executeAndParse(PreparedStatement preparedStatement) throws SQLException {
        Collection<Entity> entities = new LinkedList<>();
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                entities.add(readEntity(resultSet));
            }
            return entities;
        }
    }
}
