package entities.impl;

import entities.bt.Session;

import java.sql.Date;

public class SessionImpl implements Session {
    private long sesionId;
    private long userId;
    private Date fromDate;
    private Date tillDate;
    private String token;

    public SessionImpl() {
    }

    public SessionImpl(long sesionId) {
        this.sesionId = sesionId;
    }

    public SessionImpl(long sesionId, long userId) {
        this.sesionId = sesionId;
        this.userId = userId;
    }

    public SessionImpl(long sesionId, long userId, Date fromDate, Date tillDate) {
        this.sesionId = sesionId;
        this.userId = userId;
        this.fromDate = fromDate;
        this.tillDate = tillDate;
    }

    public SessionImpl(long sesionId, long userId, Date fromDate, Date tillDate, String token) {
        this.sesionId = sesionId;
        this.userId = userId;
        this.fromDate = fromDate;
        this.tillDate = tillDate;
        this.token = token;
    }

    public void setSesionId(long sesionId) {
        this.sesionId = sesionId;
    }

    public long getSesionId() {
        return sesionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getTillDate() {
        return tillDate;
    }

    public void setTillDate(Date tillDate) {
        this.tillDate = tillDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
