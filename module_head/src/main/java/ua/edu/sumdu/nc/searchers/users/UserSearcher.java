package ua.edu.sumdu.nc.searchers.users;

import dao.DAO;
import entities.bt.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.edu.sumdu.nc.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
@Component(value = "UserSearcher")
@Scope(scopeName = "singleton")
public class UserSearcher {
    private ApplicationContext appCtx;
    private DAO DAO;
    private Logger logger =  Logger.getRootLogger();

    public UserSearcher(@Autowired DAO DAO, @Qualifier(value = "appConfig")ApplicationContext appCtx) {
        this.DAO = DAO;
        this.appCtx = appCtx;
    }

    public User getUserByID(long userID) {
        final String getUserByIDQuery = "select * from bt_users where user_id = ?";
        try (
                Connection connection = DAO.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(getUserByIDQuery)
        ) {
            preparedStatement.setLong(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = appCtx.getBean("User", User.class);
                    if (user != null) {
                        return appCtx.getBean("Utils", Utils.class).readUser(resultSet);
                    }
                }
                logger.error("Can not find user (user_id = " + userID + ")");
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
