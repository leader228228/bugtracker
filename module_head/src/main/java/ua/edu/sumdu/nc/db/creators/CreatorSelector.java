package ua.edu.sumdu.nc.db.creators;

import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class CreatorSelector {

    private ApplicationContext applicationContext;

    public CreatorSelector(@Autowired ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Creator selectForRequest(JSONObject requestBody) throws Exception {
        try {
            return applicationContext.getBean(requestBody.getString("what"), Creator.class); // for example { "what" : "create.issue" }
        } catch (BeansException e) {
            throw new IOException("Wrong search criteria " + requestBody.getString("what"));
        }
    }
}
