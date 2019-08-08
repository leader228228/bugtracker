package ua.edu.sumdu.nc.controllers.create.parsers;

import entities.bt.Issue;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import ua.edu.sumdu.nc.Utils;

import java.sql.Date;

@Repository
public class IssueInputParser implements InputParser<JSONObject, Issue> {
    private final Utils utils;
    private final ApplicationContext applicationContext;

    public IssueInputParser(@Autowired Utils utils,@Autowired ApplicationContext applicationContext) {
        this.utils = utils;
        this.applicationContext = applicationContext;
    }

    /**
     *  Creates an {@code Issue} bean of input json
     *
     * @param           from a request body
     *
     * @return          new issue (not committed in the DB)
     * */
    @Override
    public Issue parse(JSONObject from) {
        Issue issue = applicationContext.getBean("Issue", Issue.class);
        JSONObject j = from.getJSONObject("issue");
        issue.setCreated(Date.valueOf(j.getString("created")));
        issue.setTitle(j.getString("title"));
        issue.setBody(j.getString("body"));
        issue.setReporterId(j.getLong("reporterId"));
        if (j.get("assigneeId") != null) {
            issue.setAssigneeId(j.getLong("assigneeId"));
        }
        issue.setProjectId(j.getLong("projectId"));
        return issue;
    }
}
