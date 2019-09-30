package ua.edu.sumdu.nc.controllers.search.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.bt.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.validation.search.users.SearchUsersRequest;

import javax.validation.Valid;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@RestController
public class SearchUsersController extends Controller<SearchUsersRequest> {
    public SearchUsersController(@Qualifier(value = "appConfig") ApplicationContext appCtx) {
        super(appCtx);
    }

    @Override
    public Object handle(SearchUsersRequest request) {
        try (
            Connection connection = DAO.getConnection();
            PreparedStatement preparedStatement = getPreparedStatementFor(request, connection);
            ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            Utils utils = appCtx.getBean("Utils", Utils.class);
            List<User> users = new LinkedList<>();
            List<String> _users = new LinkedList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            while (resultSet.next()) {
                users.add(utils.readUser(resultSet));
            }
            resultSet.close();
            ObjectWriter objectWriter = objectMapper.writerFor(User.class);
            StringWriter stringWriter = new StringWriter();
            for (User user : users) {
                objectWriter.writeValue(stringWriter, createUserViewForResponse(user));
                _users.add(stringWriter.toString());
                stringWriter.flush();
            }
            logger.info(
                _users.size() + " users were found for request = (" + request
                    + "), result = (" + System.lineSeparator() + _users);
            return getCommonSuccessResponse(_users.toArray(new String[0]));
        } catch (Exception e) {
            logger.error("Error during users search", e);
            return getCommonErrorResponse("Unknown error occurred");
        }
    }

    private User createUserViewForResponse(User user) {
        User userView = appCtx.getBean("User", User.class);
        userView.setFirstName(user.getFirstName());
        userView.setLastName(user.getLastName());
        userView.setUserId(user.getUserId());
        userView.setLogin("********");
        userView.setPassword("********");
        return userView;
    }

    private PreparedStatement getPreparedStatementFor(SearchUsersRequest request, Connection connection) throws SQLException {
        String query = "select * from bt_users where " +
            "(user_id in (" + arrayToString(request.getUserIds()) + ") or (1 = " + (request.getUserIds() == null ? 1 : 2) + ")) " +
            " and regexp_like(first_name, ?) " +
            " and regexp_like(last_name, ?) ";
        logger.fatal("QUERy={" + query + "}");
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        /*Булевая алгебра + костыль {*/
        preparedStatement.setString(1, request.getFirstNameRegexp() == null ? ".*" : request.getFirstNameRegexp());
        preparedStatement.setString(2, request.getLastNamesRegexp() == null ? ".*" : request.getLastNamesRegexp());
        return preparedStatement;
    }

    @RequestMapping(
        path = "/search/user",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public Object proxyMethod(@Valid @RequestBody SearchUsersRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return handle(request);
        }
        logger.error("Invalid request: " + request.toString());
        return getInvalidRequestResponse(bindingResult);
    }
}
