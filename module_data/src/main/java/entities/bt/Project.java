package entities.bt;

public interface Project extends Entity {
    long getProjectId();
    String getName();
    void setName(String name);
    long getAdminId();
    void setAdminId(long adminId);
    void setProjectId(long projectId);
}
