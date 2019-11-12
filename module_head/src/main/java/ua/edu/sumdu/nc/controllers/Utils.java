package ua.edu.sumdu.nc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.bt.Entity;
import entities.bt.Issue;
import entities.bt.Reply;
import entities.bt.User;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class Utils {

    public static final char SQL_ESCAPE_CHAR = '&';
    private static final String RESPONSE_JSON_TEMPLATE = "{\"status\":\"#status#\",\"messages\":[#messages#]}";

    public static String getPatternContains(String string) {
        return '%' + escapeRegexChars(string) + '%';
    }

    public static String escapeRegexChars(String string) {
        return string
            .replaceAll("%",SQL_ESCAPE_CHAR + "%")
            .replaceAll(String.valueOf(SQL_ESCAPE_CHAR), "" + SQL_ESCAPE_CHAR + SQL_ESCAPE_CHAR)
            .replaceAll("_", SQL_ESCAPE_CHAR + "_");
    }

    public static Collection<String> marshallEntitiesToJSON(Collection<? extends Entity> entities) throws IOException {
        List<String> result = new ArrayList<>(entities.size());
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter;
        StringWriter stringWriter = new StringWriter();
        for (Entity e : entities) {
            objectWriter = objectMapper.writerFor(e.getClass());
            objectWriter.writeValue(stringWriter, e);
            result.add(stringWriter.toString());
            stringWriter = new StringWriter();
        }
        return result;
    }

    public static String getCommonErrorResponse(String...messages) {
        return RESPONSE_JSON_TEMPLATE
            .replaceFirst("#status#", "error")
            .replaceFirst("#messages#", wrapAndJoin(messages));
    }

    public static String getCommonSuccessResponse(String...messages) {
        return RESPONSE_JSON_TEMPLATE
            .replaceFirst("#status#", "success")
            .replaceFirst("#messages#", wrapAndJoin(messages));
    }

    public static String wrapAndJoin(String...strings) {
        StringBuilder stringBuilder = new StringBuilder();
        if (strings.length != 0) {
            stringBuilder = new StringBuilder("\"").append(String.join("\", \"", strings)).append("\"");
        }
        return stringBuilder.toString();
    }

    public static String getInvalidRequestResponse(BindingResult bindingResult) {
        String [] errorMessages = new String[]{};
        if (bindingResult.hasErrors()) {
            List<String> list = new ArrayList<>(bindingResult.getAllErrors().size());
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                list.add(objectError.getCode());
            }
            errorMessages = list.toArray(new String[0]);
        }
        return getCommonErrorResponse(errorMessages);
    }

    /**
     *  Reads an issue from {@code resultSet}
     *
     * @param           resultSet a result set of {@code bt_issues} containing all the table rows
     *
     * @return          an {@code Issue} bean
     *
     * !NOTE the method does not close the result set
     * !NOTE the method expects that you have invoked {@code ResultSet.next()} method before
     * */
    public static Issue readIssue(ResultSet resultSet) throws SQLException {
        Issue issue = EntityFactory.get(Issue.class);
        issue.setIssueId(resultSet.getLong("issue_id"));
        issue.setReporterId(resultSet.getLong("reporter_id"));
        issue.setProjectId(resultSet.getLong("project_id"));
        issue.setBody(resultSet.getString("body"));
        issue.setTitle(resultSet.getString("title"));
        issue.setAssigneeId(resultSet.getLong("assignee_id"));
        issue.setCreated(resultSet.getDate("created"));
        issue.setStatusId(resultSet.getInt("status_id"));
        return issue;
    }

    public static User readUser(ResultSet resultSet) throws SQLException {
        User user = EntityFactory.get(User.class);
        user.setUserId(resultSet.getLong("user_id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        return user;
    }

    public static Reply readReply(ResultSet resultSet) throws SQLException {
        Reply reply = EntityFactory.get(Reply.class);
        reply.setAuthorId(resultSet.getLong("author_id"));
        reply.setBody(resultSet.getString("body"));
        reply.setIssueId(resultSet.getLong("issue_id"));
        reply.setReplyId(resultSet.getLong("reply_id"));
        reply.setCreated(resultSet.getDate("created"));
        return reply;
    }
}
