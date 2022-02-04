package com.example.pettobackend.models;

import com.example.pettobackend.models.enums.Roles;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("roles")
public class Role {

    @Id
    private String id;
    private Roles name;

    public Role()
    {

    }

    public Role(Roles name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Roles getName() {
        return name;
    }

    public void setName(Roles name) {
        this.name = name;
    }
}