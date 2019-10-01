package ua.edu.sumdu.nc.controllers.search.issues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.validation.search.issues.SearchIssuesRequest;

import javax.validation.Valid;
import java.io.StringWriter;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@RestController
public class SearchIssuesController extends Controller<SearchIssuesRequest> {
    public SearchIssuesController(@Qualifier(value = "appConfig") ApplicationContext appCtx) {
        super(appCtx);
    }

    @Override
    public Object handle(SearchIssuesRequest request) {
        try (
            Connection connection = DAO.getConnection();
            PreparedStatement preparedStatement = getPreparedStatementFor(request, connection);
            ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            Utils utils = appCtx.getBean("Utils", Utils.class);
            List<Issue> issues = new LinkedList<>();
            List<String> _issues = new LinkedList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            while (resultSet.next()) {
                issues.add(utils.readIssue(resultSet));
            }
            resultSet.close();
            ObjectWriter objectWriter = objectMapper.writerFor(Issue.class);
            StringWriter stringWriter = new StringWriter();
            for (Issue issue : issues) {
                objectWriter.writeValue(stringWriter, issue);
                _issues.add(stringWriter.toString());
                stringWriter.flush();
            }
            logger.info(
                _issues.size() + " issues were found for request = (" + request
                    + "), result = (" + System.lineSeparator() + _issues);
            return getCommonSuccessResponse(_issues.toArray(new String[0]));
        } catch (Exception e) {
            logger.error("Error during issue search", e);
            return getCommonErrorResponse("Unknown error occurred");
        }
    }

    private PreparedStatement getPreparedStatementFor(SearchIssuesRequest request, Connection connection) throws SQLException {
        String query =
            "select * from bt_issues i left join bt_replies r on i.issue_id = r.issue_id where " +
            "(i.issue_id in (" + arrayToString(request.getIssueIds()) + ") or (1 = " + (request.getIssueIds() == null ? 1 : 2) + ")) " +
            " and (i.reporter_id in (" + arrayToString(request.getReporterIds()) + ") or (1 = " + (request.getReporterIds() == null ? 1 : 2) + ")) " +
            " and (i.assignee_id in (" + arrayToString(request.getAssigneeIds()) + ") or (1 = " + (request.getAssigneeIds() == null ? 1 : 2) + ")) " +
            " and (i.status_id in (" + arrayToString(request.getStatusId()) + ") or (1 = " + (request.getStatusId() == null ? 1 : 2) + ")) " +
            " and (i.project_id in (" + arrayToString(request.getProjectIds()) + ") or (1 = " + (request.getProjectIds() == null ? 1 : 2) + ")) " +
            " and regexp_like(i.\"body\", ?) " +
            " and regexp_like(i.title, ?) " +
            " and (i.created >= ? or (1 = " + (request.getFrom() == null ? 1 : 2) + ")) " +
            " and (i.created <= ? or (1 = " + (request.getTo() == null ? 1 : 2) + ")) " +
            " and regexp_like(r.\"body\", ?) ";
        logger.fatal("query={" + query + "}");
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        /*Булевая алгебра + костыль {*/
        preparedStatement.setString(1, request.getBodyRegexp() == null ? ".*" : request.getBodyRegexp());
        preparedStatement.setString(2, request.getTitleRegexp() == null ? ".*" : request.getTitleRegexp());
        preparedStatement.setDate(3, request.getFrom());
        preparedStatement.setDate(4, request.getTo());
        preparedStatement.setString(5, request.getReplyBodyRegexp() == null ? ".*" : request.getReplyBodyRegexp());
        return preparedStatement;
    }

    @RequestMapping(
        path = "/search/issue",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object proxyMethod(@Valid @RequestBody SearchIssuesRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
