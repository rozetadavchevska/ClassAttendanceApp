package com.ap.classattendanceapp.data.models;

public class Attendance {
    private String attendanceId;
    private String teacherName;
    private String studentName;
    private String className;
    private String courseName;
    private boolean isStudentPresent;

    public Attendance(){}

    public Attendance(String attendanceId, String teacherName, String studentName, String className, String courseName, boolean isStudentPresent){
        this.attendanceId = attendanceId;
        this.teacherName = teacherName;
        this.studentName = studentName;
        this.className = className;
        this.courseName = courseName;
        this.isStudentPresent = isStudentPresent;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isStudentPresent() {
        return isStudentPresent;
    }

    public void setStudentPresent(boolean studentPresent) {
        isStudentPresent = studentPresent;
    }
}
