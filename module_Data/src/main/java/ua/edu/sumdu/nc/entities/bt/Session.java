package ua.edu.sumdu.nc.entities.bt;

import java.sql.Date;

public interface Session extends Entity {
    long getSesionId();
    long getUserId();
    void setUserId(long userId);
    Date getFromDate();
    void setFromDate(Date fromDate);
    Date getTillDate();
    void setTillDate(Date tillDate);
    String getToken();
    void setToken(String token);
}