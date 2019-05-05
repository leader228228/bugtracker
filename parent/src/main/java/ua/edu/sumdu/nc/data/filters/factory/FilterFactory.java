package ua.edu.sumdu.nc.data.filters.factory;

import org.json.JSONObject;
import ua.edu.sumdu.nc.data.filters.Filter;

public abstract class FilterFactory {
    public abstract Filter getFor(JSONObject jsonObject);
}
