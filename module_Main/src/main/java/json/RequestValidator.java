package json;

import org.json.JSONObject;

import javax.xml.bind.ValidationException;

public interface RequestValidator {
    void validate(JSONObject jsonObject) throws ValidationException;
}
