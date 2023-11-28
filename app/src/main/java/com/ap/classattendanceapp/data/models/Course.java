package com.ap.classattendanceapp.data.models;

import java.util.Map;

public class Course {
    private String courseId;
    private String name;
    private String description;
    private Map<String, Boolean> teacherId;
    private Map<String, Boolean> studentsIds;
    private Map<String, Boolean> classes;

    public Course(){}

    public Course(String courseId, String name, String description, Map<String, Boolean> teacherId, Map<String, Boolean> studentsIds, Map<String, Boolean> classes){
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.teacherId = teacherId;
        this.studentsIds = studentsIds;
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

    public Map<String, Boolean> getTeacherId() { return teacherId; }

    public void setTeacherId(Map<String, Boolean> teacherId) { this.teacherId = teacherId; }

    public Map<String, Boolean> getStudentsIds() {
        return studentsIds;
    }

    public void setStudentsIds(Map<String, Boolean> studentsIds) {
        this.studentsIds = studentsIds;
    }

    public Map<String, Boolean> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, Boolean> classes) { this.classes = classes; }
}
