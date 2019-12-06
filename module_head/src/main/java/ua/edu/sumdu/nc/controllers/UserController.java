package ua.edu.sumdu.nc.controllers;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.nc.Utils;
import ua.edu.sumdu.nc.entities.User;
import ua.edu.sumdu.nc.services.DBUtils;
import ua.edu.sumdu.nc.services.UserService;
import ua.edu.sumdu.nc.validation.users.CreateUserRequest;
import ua.edu.sumdu.nc.validation.users.UpdateUserRequest;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private UserService userService;

    private final Logger logger = Logger.getRootLogger();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json",
        path = "/create"
    )
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("Bad request: " + request);
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.createUser(
                request.getFirstName(),
                request.getLastName(),
                request.getLogin(),
                String.valueOf(request.getPassword().hashCode())
            );
            if (logger.isInfoEnabled()) {
                logger.info("The user (user_id = " + user.getUserID() + " ) has been successfully created");
            }
            return new ResponseEntity<>(
                "The user (user_id = " + user.getUserID() + " ) has been successfully created", HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json",
        path = "/update/{login}"
    )
    public ResponseEntity<String> updateUser(@PathVariable(name = "login") String login, @Valid @RequestBody UpdateUserRequest request, BindingResult bindingResult) {
        try {
            User user = userService.searchUserByLogin(login);
            userService.updateUser(
                user.getUserID(),
                StringUtils.isBlank(request.getFirstName()) ? user.getFirstName() : request.getFirstName(),
                StringUtils.isBlank(request.getLastName()) ? user.getLastName() : request.getLastName(),
                Objects.requireNonNull(login, "Login must not be blank"),
                StringUtils.isBlank(request.getPassword()) ? user.getPassword() : request.getPassword()
            );
            return new ResponseEntity<>(
                "The user (" + login + " ) has been successfully updated", HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
        method = RequestMethod.GET,
        produces = "application/json",
        path = "/delete/{user_ids}"
    )
    public ResponseEntity<String> deleteUsers(@PathVariable(name = "user_ids") long [] userIDs) {
        if (userIDs.length == 0) {
            logger.error("Any user_id is detected");
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
        try {
            userService.deleteUsers(userIDs);
            if (logger.isInfoEnabled()) {
                logger.info("The users (user_id in " + Arrays.toString(userIDs) + ") are deleted");
            }
            return new ResponseEntity<>("The users are deleted (which existed)", HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
        method = RequestMethod.GET,
        produces = "application/json",
        path = "/search/id/{user_ids}"
    )
    public ResponseEntity<String> searchUsersByIDs(@PathVariable(name = "user_ids") long [] userIDs) {
        try {
            if (userIDs.length == 0) {
                if (logger.isInfoEnabled()) {
                    logger.info("Any ID is passed, returning all the users");
                }
                return new ResponseEntity<>(
                    Utils.getCommonSuccessResponse(Utils.buildForResponse(userService.getAll())),
                    HttpStatus.OK
                );
            }
            Collection<User> userViews = userService.searchUsersByIDs(userIDs).stream()
                .map(DBUtils::createUserView).collect(Collectors.toList());
            if (logger.isInfoEnabled()) {
                logger.info("Found users " + userViews);
            }
            return new ResponseEntity<>(
                Utils.getCommonSuccessResponse(Utils.buildForResponse(userViews)),
                HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
        method = RequestMethod.GET,
        produces = "application/json",
        path = "/search/name/{name}"
    )
    public ResponseEntity<String> searchUsersByName(@PathVariable(name = "name") String name) {
        try {
            if (StringUtils.isBlank(name)) {
                if (logger.isInfoEnabled()) {
                    logger.info("Empty string is passed, returning all the users");
                }
                return new ResponseEntity<>(
                    Utils.getCommonSuccessResponse(Utils.buildForResponse(userService.getAll())),
                    HttpStatus.OK
                );
            }
            Collection<User> userViews = userService.searchUsersByName(name).stream()
                .map(DBUtils::createUserView).collect(Collectors.toList());
            if (logger.isInfoEnabled()) {
                logger.info("Found users " + userViews);
            }
            return new ResponseEntity<>(
                Utils.getCommonSuccessResponse(Utils.buildForResponse(userViews)),
                HttpStatus.OK
            );
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
