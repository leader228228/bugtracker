package ua.edu.sumdu.nc.controllers.search.issues;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.bt.Entity;
import entities.bt.Issue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.searchers.issues.IssueSearcher;
import ua.edu.sumdu.nc.validation.search.issues.SearchIssuesRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.*;
import java.util.*;

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

    private PreparedStatement getPreparedStatementFor(SearchIssuesRequest request, Connection connection)
        throws SQLException {
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
        preparedStatement.setString(
            5,
            request.getReplyBodyRegexp() == null ? ".*" : request.getReplyBodyRegexp()
        );
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

    @GetMapping(path = "/search/issue/id/{id}" ,produces = "application/json")
    public Object proxyMethod(@PathVariable(name = "id") long issueId) {
        IssueSearcher issueSearcher = appCtx.getBean("IssueSearcher", IssueSearcher.class);
        Issue issue = issueSearcher.getIssueByID(issueId);
        if (issue == null) {
            logger.info("Can not find issue (id = " + issueId + ")");
            return getCommonErrorResponse("Can not find issue (id = " + issueId + ")");
        }
        logger.info("Issue (id = " + issueId + ") has been found");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerFor(Issue.class);
        StringWriter stringWriter = new StringWriter();
        try {
            objectWriter.writeValue(stringWriter, issue);
        } catch (Exception e) {
            logger.error("Error during marshalling issue to json", e);
            return getCommonErrorResponse("Issue found", "Error occurred during it processing");
        }
        return getCommonSuccessResponse("Issue found", stringWriter.toString());
    }

    private String getPattern(String string) {
        return '%' + escapeRegexChars(string) + '%';
    }

    private boolean checkByWhat(String byWhat) {
        Set<String> supportedFileds = new HashSet<>(Arrays.asList("body", "title"));
        return supportedFileds.contains(byWhat);
    }

    @GetMapping(path = "/search/issue/{byWhat}/{string}", produces = "application/json")
    public Object proxyMethod(
        @PathVariable(name = "string") String title,
        @PathVariable(name = "byWhat") String byWhat) {
        if (!checkByWhat(byWhat)) {
            logger.error("The attempt to search issues by unknown field (" + byWhat + ") failed");
            return getCommonErrorResponse("Can not recognize field " + byWhat);
        }
        String selectQuery =
            "select * from bt_issues where lower(" + byWhat + ") like lower(?) escape ?";
        try (Connection connection = DAO.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            String pattern = getPattern(title);
            preparedStatement.setString(1, pattern);
            preparedStatement.setString(2, String.valueOf(escapeChar));
            List<Issue> issues = new LinkedList<>();
            Utils utils = appCtx.getBean("Utils", Utils.class);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    issues.add(utils.readIssue(resultSet));
                }
            }
            logger.error(issues.size() + " issues found");
            return getCommonSuccessResponse(marshallEntitiesToJSON(issues).toArray(new String[0]));
        } catch (SQLException | IOException e) {
            logger.error("Exception during issue searching", e);
            return getCommonErrorResponse("Error during issue searching");
        }
    }

    @Override
    protected Class<? extends Entity> getClassForMarshalling() {
        return appCtx.getBean("Issue", Issue.class).getClass();
    }

    private String marshallIssueToJSON(Issue issue) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerFor(Issue.class);
        StringWriter stringWriter = new StringWriter();
        objectWriter.writeValue(stringWriter, issue);
        return stringWriter.toString();
    }
}
