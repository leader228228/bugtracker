package ua.edu.sumdu.nc.services;

import ua.edu.sumdu.nc.entities.User;

public class DBUtils {

    private static final char SQL_ESCAPE_CHAR = '&';

    public static String getPatternContains(String string) {
        return '%' + escapeRegexChars(string) + '%';
    }

    private static String escapeRegexChars(String string) {
        return string
            .replaceAll("%",SQL_ESCAPE_CHAR + "%")
            .replaceAll(String.valueOf(SQL_ESCAPE_CHAR), "" + SQL_ESCAPE_CHAR + SQL_ESCAPE_CHAR)
            .replaceAll("_", SQL_ESCAPE_CHAR + "_");
    }

    /**
     * @return          an instance of {@code User} with hidden login and password. The instance is immutable,
     *                  throws {@code UnsupportedOperationException} if you try to update its fields
     * */
    public static User createUserView(User user) {
        return new User() {
            @Override
            public long getUserID() {
                return user.getUserID();
            }

            @Override
            public String getFirstName() {
                return user.getFirstName();
            }

            @Override
            public void setFirstName(String firstName) {
                throw new UnsupportedOperationException("This object is a read-only user view");
            }

            @Override
            public String getLastName() {
                return user.getLastName();
            }

            @Override
            public void setLastName(String lastName) {
                throw new UnsupportedOperationException("This object is a read-only user view");
            }

            @Override
            public String getLogin() {
                return "**********";
            }

            @Override
            public void setLogin(String login) {
                throw new UnsupportedOperationException("This object is a read-only user view");
            }

            @Override
            public String getPassword() {
                return "**********";
            }

            @Override
            public void setPassword(String password) {
                throw new UnsupportedOperationException("This object is a read-only user view");
            }

            @Override
            public void setUserID(long userId) {
                throw new UnsupportedOperationException("This object is a read-only user view");
            }
        };
    }
}
