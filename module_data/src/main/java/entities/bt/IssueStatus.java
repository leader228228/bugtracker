package entities.bt;

public interface IssueStatus extends Entity {
    int getStatusId();
    String getValue();
    void setValue(String newValue);
    void setStatusId(int statusId);
}