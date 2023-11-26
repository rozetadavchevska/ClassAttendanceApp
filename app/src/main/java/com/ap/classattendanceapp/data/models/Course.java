package com.ap.classattendanceapp.data.models;

import java.util.List;

public class Course {
    private String courseId;
    private String name;
    private String description;
    private List<String> teacherIds;
    private List<String> studentIds;
    private List<String> classes;

    public Course(){}

    public Course(String courseId, String name, String description, List<String> teacherIds, List<String> studentIds, List<String> classes){
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.teacherIds = teacherIds;
        this.studentIds = studentIds;
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

    public List<String> getTeacherIds() { return teacherIds; }

    public void setTeacherIds(List<String> teacherIds) { this.teacherIds = teacherIds; }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) { this.classes = classes; }
}
