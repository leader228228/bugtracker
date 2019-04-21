package ua.edu.sumdu.nc.entities.bt;

public interface Project extends Entity {
    long getProjectId();
    String getName();
    void setName(String name);
    long getAdminId();
    void setAdminId(long adminId);
}