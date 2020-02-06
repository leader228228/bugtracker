package ua.edu.sumdu.nc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ua.edu.sumdu.nc.entities.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Utils {

    public static String getCommonErrorResponse(String...messages) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode _messages = mapper.createArrayNode();
        Arrays.stream(messages).forEach(_messages::add);
        ObjectNode successResponse = mapper.createObjectNode();
        successResponse.put("status", "error");
        successResponse.set("messages", _messages);
        return successResponse.toString();
    }

    public static String getCommonSuccessResponse(String...messages) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode _messages = mapper.createArrayNode();
        Arrays.stream(messages).forEach(_messages::add);
        ObjectNode successResponse = mapper.createObjectNode();
        successResponse.put("status", "success");
        successResponse.set("messages", _messages);
        return successResponse.toString();
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

    public static String buildForResponse(Collection<? extends Entity> _entities) throws JsonProcessingException {
        return new ObjectMapper().writer().writeValueAsString(new Object() {
            /** @noinspection unused*/
            public Collection<? extends Entity> entities = _entities;
        });
    }
}
