package ua.edu.sumdu.nc.entities;

public class EntityFactory {

    /** @noinspection unchecked*/
    public static <T extends Entity> T get(Class<T> clazz) {
        if (clazz.equals(Issue.class)) {
            return (T) new Issue(){};
        } else if (clazz.equals(Project.class)) {
            return (T) new Project(){};
        } else if (clazz.equals(User.class)) {
            return (T) new User(){};
        } else if (clazz.equals(IssueStatus.class)) {
            return (T) new IssueStatus(){};
        } else if (clazz.equals(Reply.class)) {
            return (T) new Reply(){};
        } else {
            throw new IllegalArgumentException("Expected one of Entity successors, found " + clazz);
        }
    }
}
