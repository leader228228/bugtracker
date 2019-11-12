package ua.edu.sumdu.nc.controllers;

import entities.bt.*;
import entities.impl.*;

public class EntityFactory {

    public static <T extends Entity> T get(Class<T> clazz) {
        if (clazz.equals(Issue.class)) {
            return (T) new IssueImpl();
        } else if (clazz.equals(Project.class)) {
            return (T) new ProjectImpl();
        } else if (clazz.equals(User.class)) {
            return (T) new UserImpl();
        } else if (clazz.equals(IssueStatus.class)) {
            return (T) new IssueStatusImpl();
        } else if (clazz.equals(Reply.class)) {
            return (T) new ReplyImpl();
        } else {
            throw new IllegalArgumentException("Expected one of Entity successors, found " + clazz);
        }
    }
}
