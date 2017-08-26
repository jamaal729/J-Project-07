package com.teamtreehouse.instateam.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, max = 30)
    private String name;

    @ManyToOne
    private Role role;

    public Collaborator() { }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    @Override
    public String toString() {
        return "Collaborator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}

/*Create the Collaborator model class, which represents a person who is a candidate for working on any given project. Each collaborator should contain the following:
        id: auto-generated numeric identifier to serve as the table’s primary key
        name: first and last name of the collaborator. This is a required field for data validation.
        role: the single Role object that represents this collaborator’s skill. For proper data association, it’s important to keep in mind that there could be many collaborators associated with any one role. This is a required field for data validation.
        Getters and setters for all fields
        Default constructor*/
