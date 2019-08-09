package ua.edu.sumdu.nc.db.filters;

import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class FilterSelector {
    private final ApplicationContext applicationContext;

    public FilterSelector(@Autowired ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public Filter filterForRequest(JSONObject requestBody) throws Exception {
        String searchWhat = requestBody.getString("what");
        try {
            return applicationContext.getBean(searchWhat, Filter.class);
        } catch (BeansException e) {
            throw new IOException("Wrong search criteria " + requestBody.getString("what")); // for example { "what" : "search.issue.byId" }
        }
    }
}
