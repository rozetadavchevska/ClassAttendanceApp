package com.ap.classattendanceapp.data.models;

import java.util.List;

public class Course {
    private String courseId;
    private String name;
    private String description;
    private List<String> teacherId;
    private List<String> students;
    private List<String> classes;

    public Course(){}

    public Course(String courseId, String name, String description, List<String> teacherId, List<String> students, List<String> classes){
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.teacherId = teacherId;
        this.students = students;
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

    public List<String> getTeacherId() { return teacherId; }

    public void setTeacherId(List<String> teacherId) { this.teacherId = teacherId; }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) { this.classes = classes; }
}
