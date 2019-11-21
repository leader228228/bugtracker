package ua.edu.sumdu.nc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.bt.Entity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class Utils {

    private static final String RESPONSE_JSON_TEMPLATE = "{\"status\":\"#status#\",\"messages\":[#messages#]}";

    public static String getCommonErrorResponse(String...messages) {
        return RESPONSE_JSON_TEMPLATE
            .replaceFirst("#status#", "error")
            .replaceFirst("#messages#", wrapAndJoin(messages));
    }

    public static String getCommonSuccessResponse(String...messages) {
        return RESPONSE_JSON_TEMPLATE
            .replaceFirst("#status#", "success")
            .replaceFirst("#messages#", wrapAndJoin(messages));
    }

    public static String wrapAndJoin(String...strings) {
        StringBuilder stringBuilder = new StringBuilder();
        if (strings.length != 0) {
            stringBuilder = new StringBuilder("\"").append(String.join("\", \"", strings)).append("\"");
        }
        return stringBuilder.toString();
    }

    public static String getInvalidRequestResponse(BindingResult bindingResult) {
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

    public static String buildForResponse(Collection<? extends Entity> issues) throws JsonProcessingException {
        return new ObjectMapper().writer().writeValueAsString(new Object() {
            public Collection<? extends Entity> _issues = issues;
        });
    }
}
