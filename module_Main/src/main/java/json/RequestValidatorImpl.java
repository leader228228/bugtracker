package json;


import org.apache.commons.io.FileUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class RequestValidatorImpl implements RequestValidator {
    private static Schema BT_REQUEST_SCHEMA;
    static {
        try {
            BT_REQUEST_SCHEMA = SchemaLoader
                    .builder()
                    .resolutionScope(RequestValidatorImpl.class.getResource(
                    "../json/schemas/").toURI()) // root schemas folder URI
                    .schemaClient(SchemaClient.classPathAwareClient())
                    .schemaJson(new JSONObject(FileUtils.readFileToString(new File(
                        RequestValidatorImpl.class.getResource(
                    "../json/schemas/BTRequest.json").toURI())))) // the main request schema by itself
                    .build()
                    .load()
                    .build();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void validate(JSONObject jsonObject) throws ValidationException {
        BT_REQUEST_SCHEMA.validate(jsonObject);
    }
}
