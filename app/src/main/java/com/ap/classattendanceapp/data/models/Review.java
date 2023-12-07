package com.ap.classattendanceapp.data.models;

public class Review {
    private String reviewId;
    private String timePace;
    private String orgPresentation;
    private String importance;
    private String comment;
    private String classId;
    private String className;
    private String teacherName;
    private String courseName;


    public Review(String reviewId, String timePace, String orgPresentation, String importance, String comment, String classId, String className, String teacherName, String courseName) {
        this.reviewId = reviewId;
        this.timePace = timePace;
        this.orgPresentation = orgPresentation;
        this.importance = importance;
        this.comment = comment;
        this.classId = classId;
        this.className = className;
        this.teacherName = teacherName;
        this.courseName = courseName;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
