package com.ap.classattendanceapp.data.models;

public class Attendance {
    private String attendanceId;
    private String teacherId;
    private String studentId;
    private String classId;
    private String courseId;
    private boolean isPresent;

    public Attendance(){}

    public Attendance(String attendanceId, String studentId, String classId, String courseId, boolean isPresent){
        this.attendanceId = attendanceId;
        this.studentId = studentId;
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

    public String getTeacherId() { return teacherId; }

    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String userId) {
        this.studentId = studentId;
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
