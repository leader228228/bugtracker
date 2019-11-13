package entities.bt;

public interface Project extends Entity {
    String getName();
    void setName(String name);
    long getProjectID();
    void setProjectID(long projectId);
}
