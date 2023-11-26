package com.ap.classattendanceapp.data.models;

import java.util.Date;
import java.util.List;

public class Class {
    private String classId;
    private String name;
    private String description;
    private Date dateTime;
    private String teacherId;
    private List<String> studentIds;

    public Class(){}

    public Class(String classId, String name, String description, Date dateTime, String teacherId, List<String> studentIds){
        this.classId = classId;
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.teacherId = teacherId;
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

    public Date getDateTime() { return dateTime; }

    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

    public String getTeacherId() { return teacherId; }

    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }
}
