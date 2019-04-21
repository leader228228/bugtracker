package ua.edu.sumdu.nc.entities.bt;

public interface IssueStatus extends Entity {
    long getStatusId();
    String getValue();
    void setValue(String newValue);
}