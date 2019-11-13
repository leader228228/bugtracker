package entities.impl;

import entities.bt.IssueStatus;
import org.springframework.stereotype.Component;

@Component
public class IssueStatusImpl implements IssueStatus {
    private int statusId;
    private String value;

    public IssueStatusImpl() {
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

    @Override
    public String toString() {
        return "IssueStatusImpl{" +
                "statusId=" + statusId +
                ", value='" + value + '\'' +
                '}';
    }
}
