package ua.edu.sumdu.nc.entities.impl;

import ua.edu.sumdu.nc.entities.bt.User;

public class UserImpl implements User {
    private long userId;
    private String firstName;
    private String lastName;
    private String login;
    private String password;

    public UserImpl(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void save() {
        // TODO
    }

    @Override
    public void delete() {
        // TODO
    }
}