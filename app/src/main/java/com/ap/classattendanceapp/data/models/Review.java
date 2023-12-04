package com.ap.classattendanceapp.data.models;

public class Review {
    private String timePace;
    private String orgPresentation;
    private String importance;
    private String comment;
    private String studentId;
    private String teacherId;
    private String courseId;


    public Review(String timePace, String orgPresentation, String importance, String comment, String studentId, String teacherId, String courseId) {
        this.timePace = timePace;
        this.orgPresentation = orgPresentation;
        this.importance = importance;
        this.comment = comment;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.courseId = courseId;
    }


    public String getTimePace() {
        return timePace;
    }

    public void setTimePace(String timePace) {
        this.timePace = timePace;
    }

    public String getOrgPresentation() {
        return orgPresentation;
    }

    public void setOrgPresentation(String orgPresentation) {
        this.orgPresentation = orgPresentation;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
