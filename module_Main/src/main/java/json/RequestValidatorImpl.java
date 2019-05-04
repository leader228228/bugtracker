package json;


import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class RequestValidatorImpl implements RequestValidator {
    private static final String BT_REQUEST_SCHEMA = "BT_REQUEST_SCHEMA";
    private static final String BT_JSON_SCHEMAS_CLASSPATH = "BT_JSON_SCHEMAS_CLASSPATH";
    private static final Map<String, Schema> schemas = new HashMap<>();
    static {
        init();
    }
    @Override
    public void validate(JSONObject jsonObject) throws ValidationException {
        schemas.get(BT_REQUEST_SCHEMA).validate(jsonObject);
    }

    private static void init() {
        Schema requestSchema = SchemaLoader
                .builder()
                .resolutionScope(System.getProperties().getProperty(BT_JSON_SCHEMAS_CLASSPATH)) // root schemas folder URI
                .schemaClient(SchemaClient.classPathAwareClient())
                .schemaJson(new JSONObject(System.getProperties().getProperty(BT_REQUEST_SCHEMA))) // the schema by itself
                .build()
                .load()
                .build();
        schemas.put(BT_REQUEST_SCHEMA, requestSchema);
    }
}
