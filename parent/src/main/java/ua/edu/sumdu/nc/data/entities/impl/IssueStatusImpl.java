package ua.edu.sumdu.nc.data.entities.impl;

import org.springframework.stereotype.Component;
import ua.edu.sumdu.nc.data.entities.bt.IssueStatus;

@Component
public class IssueStatusImpl implements IssueStatus {
    private int statusId;
    private String value;

    public IssueStatusImpl() {
    }

    public IssueStatusImpl(int statusId) {
        this.statusId = statusId;
    }

    public IssueStatusImpl(int statusId, String value) {
        this.statusId = statusId;
        this.value = value;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @Override
    public int getStatusId() {
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

}
