package ua.edu.sumdu.nc.jobs;

import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

public class SubmitIssueJob implements Job {
    @Autowired
    @Qualifier(value = "Issue")
    private Issue issue;
    /**
     *  Creates an {@code Issue} and commits it to the DB
     *
     * @param           map is {@code Map<String, Object>} containing issue parameters
     *
     * @return          a {@code Collections.singletonMap(...)} containing {@code Long} issue_id of submitted issue
     *
     * @throws          java.io.IOException if an I/O error occurred
     *                  (e.g. {@code map} does not contain a mandatory attribute value)
     */
    @Override
    public Map execute(Map map) throws Exception {
        try {
            issue.setIssueId(Long.parseLong((String) map.get("issue_id")));
            issue.setTitle((String) map.get("title"));
            issue.setBody((String) map.get("body"));
        } catch (ClassCastException | NumberFormatException e) {
            throw new IOException(e);
        }
    }
}
