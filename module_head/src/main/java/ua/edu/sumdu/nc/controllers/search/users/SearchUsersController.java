package ua.edu.sumdu.nc.controllers.search.users;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.bt.Entity;
import entities.bt.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.controllers.Controller;
import ua.edu.sumdu.nc.validation.search.users.SearchUsersRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
                objectWriter.writeValue(stringWriter,
                    Collections.singletonList(createUserViewsForResponse(Collections.singletonList(user))).get(0));
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

    private PreparedStatement getPreparedStatementFor(SearchUsersRequest request, Connection connection)
        throws SQLException {
        String query = "select * from bt_users where " +
            "(user_id in (" + arrayToString(request.getUserIds()) + ") or (1 = " + (request.getUserIds() == null ? 1 : 2) + ")) " +
            " and regexp_like(first_name, ?) " +
            " and regexp_like(last_name, ?) ";
        logger.fatal("QUERy={" + query + "}");
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        /*Булевая алгебра + костыль {*/
        preparedStatement.setString(
            1,
            request.getFirstNameRegexp() == null ? ".*" : request.getFirstNameRegexp()
        );
        preparedStatement.setString(
            2,
            request.getLastNamesRegexp() == null ? ".*" : request.getLastNamesRegexp()
        );
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

    @GetMapping(
        path = "/search/user/name/{name}",
        produces = "application/json"
    )
    public Object proxyMethod(@PathVariable(name = "name") String name) {
        String query = "select * from bt_users where lower(first_name || ' ' || last_name) like lower(?) escape ?";
        try (
            Connection connection = DAO.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, '%' + escapeRegexChars(name) + '%');
            preparedStatement.setString(2, String.valueOf(escapeChar));
            Collection<User> userViews =
                createUserViewsForResponse((Collection<User>) executeAndParse(preparedStatement));
            logger.info(userViews.size() + " users found");
            return getCommonSuccessResponse(marshallEntitiesToJSON(userViews).toArray(new String[0]));
        } catch (SQLException | IOException e) {
            logger.error("Error occurred during searching users", e);
            return getCommonErrorResponse("Error occurred during searching users");
        }
    }

    @Override
    protected Class<? extends Entity> getClassForMarshalling() {
        //return appCtx.getBean("User", User.class).getClass();
        return UserView.class;
    }

    @Override
    protected Entity readEntity(ResultSet resultSet) throws SQLException {
        return appCtx.getBean("Utils", Utils.class).readUser(resultSet);
    }

    private static class UserView implements User {
        private final String escaped = "********";
        private final User user;

        UserView(User user) {
            this.user = user;
        }

        @JsonGetter
        @Override
        public long getUserId() {
            return user.getUserId();
        }

        @JsonGetter
        @Override
        public String getFirstName() {
            return user.getFirstName();
        }

        @JsonGetter
        @Override
        public void setFirstName(String firstName) {
            throw new UnsupportedOperationException("Can not set new name to this user view");
        }

        @JsonGetter
        @Override
        public String getLastName() {
            return user.getLastName();
        }

        @Override
        public void setLastName(String lastName) {
            throw new UnsupportedOperationException("Can not set new name to this user view");
        }

        @JsonGetter
        @Override
        public String getLogin() {
            return escaped;
        }

        @Override
        public void setLogin(String login) {
            throw new UnsupportedOperationException("Can not set new login to this user view");
        }

        @JsonGetter
        @Override
        public String getPassword() {
            return escaped;
        }

        @Override
        public void setPassword(String password) {
            throw new UnsupportedOperationException("User view can not have password");
        }

        @Override
        public void setUserId(long userId) {
            throw new UnsupportedOperationException("Can not set new user id to this user view");
        }

        @Override
        public void update() throws SQLException {
            throw new UnsupportedOperationException("Can not update this user view");
        }

        @Override
        public void save() throws SQLException {
            throw new UnsupportedOperationException("Can not save this user view");
        }

        @Override
        public void delete() throws SQLException {
            throw new UnsupportedOperationException("Can not delete this user view");
        }
    }

    private Collection<User> createUserViewsForResponse(Collection<User> users) {
        Collection<User> _users = new ArrayList<>(users.size());
        for (User user : users) {
            _users.add(new UserView(user));
        }
        return _users;
    }
}
