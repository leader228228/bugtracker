package ua.edu.sumdu.nc.data.filters.factory;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import ua.edu.sumdu.nc.data.filters.Filter;

import java.util.HashMap;
import java.util.Map;

public class FilterFactoryImpl extends FilterFactory {
    @Autowired
    private static Map<String, Filter> filters;

    @Override
    public Filter getFor(JSONObject jsonObject) {
        return null; // TODO
    }

    public static Map<String, Filter> getFilters() {
        return filters;
    }

    public static void setFilters(Map<String, Filter> filters) {
        FilterFactoryImpl.filters = filters;
    }
}
