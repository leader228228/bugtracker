package filters.factory;

import org.json.JSONObject;
import filters.Filter;

public abstract class FilterFactory {
    public abstract Filter getFor(JSONObject jsonObject);
}
