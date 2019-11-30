package entities;

public abstract class IssueStatus implements Entity {

    private int statusID;
    private String value;

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "IssueStatus{" +
            "statusID=" + statusID +
            ", value='" + value + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssueStatus that = (IssueStatus) o;

        if (statusID != that.statusID) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = statusID;
        result = 31 * result + value.hashCode();
        return result;
    }
}