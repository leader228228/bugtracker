package entities.bt;

public interface User extends Entity {
    long getUserId();
    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    String getLogin();
    void setLogin(String login);
    String getPassword();
    void setPassword(String password);
    void setUserId(long userId);
}
