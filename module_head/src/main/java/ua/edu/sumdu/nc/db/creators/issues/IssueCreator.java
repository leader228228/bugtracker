package ua.edu.sumdu.nc.db.creators.issues;

import dao.DAO;
import entities.bt.Issue;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.db.creators.Creator;
import ua.edu.sumdu.nc.db.dbparsers.issues.IssueDBParser;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;

public class IssueCreator implements Creator<Collection<Issue>> {
    private Collection<JSONObject> jsonObjects;
    private DAO dao;
    private IssueDBParser issueDBParser;

    public IssueCreator(@Autowired DAO dao, @Autowired IssueDBParser issueDBParser) {
        this.dao = dao;
        this.issueDBParser = issueDBParser;
    }

    public Collection<JSONObject> getJsonObjects() {
        return jsonObjects;
    }

    public void setJsonObjects(Collection<JSONObject> jsonObjects) {
        this.jsonObjects = jsonObjects;
    }

    @Override
    public Collection<Issue> create() throws Exception {
        try (Connection connection = dao.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(Utils.QUERY_INSERT_ISSUE);
            Long issuesId [] = new Long[jsonObjects.size()];
            for (JSONObject jsonObject : jsonObjects) {
                preparedStatement.setString(1, jsonObject.getString("title"));
                preparedStatement.setString(2, jsonObject.getString("body"));
                preparedStatement.setLong(3, jsonObject.getLong("reporter_id"));
                preparedStatement.setObject(4, jsonObject.get("assignee_id"));
                preparedStatement.setDate(5, Date.valueOf(jsonObject.getString("created")));
                preparedStatement.setInt(6, jsonObject.getInt("status_id"));
                preparedStatement.setLong(7, jsonObject.getLong("project_id"));
                preparedStatement.executeUpdate();
            }
            preparedStatement = connection.prepareStatement(Utils.QUERY_SELECT_ISSUES_BY_ID);
            preparedStatement.setArray(1, connection.createArrayOf("NUMBER", issuesId));
            return issueDBParser.parse(preparedStatement.executeQuery());
        }
    }
}
