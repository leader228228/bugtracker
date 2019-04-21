package ua.edu.sumdu.nc.entities.impl;

import ua.edu.sumdu.nc.entities.bt.IssueStatus;

public class IssueStatusImpl implements IssueStatus {
    private int statusId;
    private String value;

    public IssueStatusImpl(int statusId) {
        this.statusId = statusId;
    }

    public IssueStatusImpl(int statusId, String value) {
        this.statusId = statusId;
        this.value = value;
    }

    @Override
    public long getStatusId() {
        return statusId;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String newValue) {
        value = newValue;
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