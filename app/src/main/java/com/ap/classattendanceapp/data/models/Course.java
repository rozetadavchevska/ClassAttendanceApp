package com.ap.classattendanceapp.data.models;

import java.util.List;

public class Course {
    private String courseId;
    private String name;
    private String description;
    private List<String> users;
    private List<String> classes;

    public Course(){}

    public Course(String courseId, String name, String description, List<String> users, List<String> classes){
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.users = users;
        this.classes = classes;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }
}
