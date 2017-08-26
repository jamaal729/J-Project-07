package com.teamtreehouse.instateam.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 3, max = 30)
    private String name;

    @Size(min = 3, max = 30)
    private String description;

    private String status;

    @ManyToMany
    private List<Role> rolesNeeded;

    @ManyToMany
    private List<Collaborator> collaborators;

    public Project() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Role> getRolesNeeded() { return rolesNeeded; }

    public void setRolesNeeded(List<Role> rolesNeeded) {
        this.rolesNeeded = rolesNeeded;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", rolesNeeded=" + rolesNeeded +
                ", collaborators=" + collaborators +
                '}';
    }
}

/*Create the Project model class, which represents a project for which a project manager is seeking collaborators. Each project should contain the following:
        id: auto-generated numeric identifier to serve as the table’s primary key
        name: alphanumeric, reader-friendly name to be displayed. This is a required field for data validation.
        description: longer description of the project. This is a required field for data validation.
        status: alphanumeric status of the project, for example “recruiting” or “on hold”
        rolesNeeded: collection of Role objects representing all roles needed for this project, regardless of whether or not they’ve been filled. For proper data association, keep in mind that there could be many projects that contain many Role objects. That is, each project can have many roles that it needs, and each role can be needed by many projects.
        collaborators: collection of Collaborator objects representing any collaborators that have been assigned to this project. For data association, use the fact that there could be many projects that contain many Collaborator objects. That is, each project can have many collaborators, and each collaborator can work on many projects.
        Getters and setters for all fields
        Default constructor*/
