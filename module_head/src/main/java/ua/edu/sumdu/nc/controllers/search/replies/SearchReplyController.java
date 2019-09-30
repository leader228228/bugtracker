package ua.edu.sumdu.nc.controllers.search.replies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.bt.Issue;
import entities.bt.Reply;
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
import ua.edu.sumdu.nc.validation.search.replies.SearchRepliesRequest;

import javax.validation.Valid;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@RestController
public class SearchReplyController extends Controller<SearchRepliesRequest> {
    public SearchReplyController(@Qualifier(value = "appConfig") ApplicationContext appCtx) {
        super(appCtx);
    }

    @Override
    public Object handle(SearchRepliesRequest request) {
        try (
            Connection connection = DAO.getConnection();
            PreparedStatement preparedStatement = getPreparedStatementFor(request, connection);
            ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            Utils utils = appCtx.getBean("Utils", Utils.class);
            List<Reply> replies = new LinkedList<>();
            List<String> _replies = new LinkedList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            while (resultSet.next()) {
                replies.add(utils.readReply(resultSet));
            }
            resultSet.close();
            ObjectWriter objectWriter = objectMapper.writerFor(Reply.class);
            StringWriter stringWriter = new StringWriter();
            for (Reply reply : replies) {
                objectWriter.writeValue(stringWriter, reply);
                _replies.add(stringWriter.toString());
                stringWriter.flush();
            }
            logger.info(
                _replies.size() + " replies were found for request = (" + request
                    + "), result = (" + System.lineSeparator() + _replies);
            return getCommonSuccessResponse(_replies.toArray(new String[0]));
        } catch (Exception e) {
            logger.error("Error during replies search", e);
            return getCommonErrorResponse("Unknown error occurred");
        }
    }

    private PreparedStatement getPreparedStatementFor(SearchRepliesRequest request, Connection connection) throws SQLException {
        String query = "select * from bt_replies where " +
            "(reply_id in (" + arrayToString(request.getReplyIds()) + ") or (1 = " + (request.getReplyIds() == null ? 1 : 2) + ")) " +
            " and (author_id in (" + arrayToString(request.getAuthorIds()) + ") or (1 = " + (request.getAuthorIds() == null ? 1 : 2) + ")) " +
            " and (issue_id in (" + arrayToString(request.getIssueIds()) + ") or (1 = " + (request.getIssueIds() == null ? 1 : 2) + ")) " +
            " and regexp_like(\"body\", ?) ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        /*Булевая алгебра + костыль {*/
        preparedStatement.setString(1, request.getBodyRegexp() == null ? ".*" : request.getBodyRegexp());
        return preparedStatement;
    }

    @RequestMapping(
        path = "/search/reply",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object proxyMethod(@Valid @RequestBody SearchRepliesRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
