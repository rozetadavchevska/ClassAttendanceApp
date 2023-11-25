package com.ap.classattendanceapp.data.models;

public class Attendance {
    private String attendanceId;
    private String userId;
    private String classId;
    private String courseId;
    private boolean isPresent;

    public Attendance(){}

    public Attendance(String attendanceId, String userId, String classId, String courseId, boolean isPresent){
        this.attendanceId = attendanceId;
        this.userId = userId;
        this.classId = classId;
        this.courseId = courseId;
        this.isPresent = isPresent;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
