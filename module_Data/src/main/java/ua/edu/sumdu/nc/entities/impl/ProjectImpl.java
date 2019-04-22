package ua.edu.sumdu.nc.entities.impl;

import ua.edu.sumdu.nc.entities.bt.Project;

public class ProjectImpl implements Project {
    private long projectId;
    private String name;
    private long adminId;

    public ProjectImpl(long projectId) {
        this.projectId = projectId;
    }

    public ProjectImpl(long projectId, String name) {
        this.projectId = projectId;
        this.name = name;
    }

    public ProjectImpl(long projectId, String name, long adminId) {
        this.projectId = projectId;
        this.name = name;
        this.adminId = adminId;
    }

    public long getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

}
