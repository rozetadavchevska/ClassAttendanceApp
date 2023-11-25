package com.ap.classattendanceapp.data.models;

import java.util.List;

public class Class {
    private String classId;
    private String name;
    private String description;
    private List<String> users;

    public Class(){}

    public Class(String classId, String name, String description, List<String> users){
        this.classId = classId;
        this.name = name;
        this.description = description;
        this.users = users;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
