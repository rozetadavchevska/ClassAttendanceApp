package com.ap.classattendanceapp.data.models;

import java.util.List;

public class Class {
    private String classId;
    private String name;
    private String description;
    private String date;
    private String time;
    private String teacherId;
    private String courseId;
    private List<String> studentIds;

    public Class(){}

    public Class(String classId, String name, String description, String date, String time, String teacherId, String courseId, List<String> studentIds){
        this.classId = classId;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time= time;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.studentIds = studentIds;
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

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getTeacherId() { return teacherId; }

    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getCourseId() { return courseId; }

    public void setCourseId(String courseId) { this.courseId = courseId; }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) { this.studentIds = studentIds; }
}
