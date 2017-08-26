package com.teamtreehouse.instateam.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, max = 30)
    private String name;

    public Role() { }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

/*Create the Role model class, which represents the roles each project could contain, and that need to be filled. Each role will have the following pieces of information associated with it:
        id: auto-generated numeric identifier to serve as the table’s primary key
        name: alphanumeric, reader-friendly name to be displayed. Example role names might be “developer”, “designer”, or “QA engineer”. This is a required field for data validation.
        Getters and setters for all fields
        Default constructor*/
