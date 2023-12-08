package com.ap.classattendanceapp.data.models;

public class Attendance {
    private String attendanceId;
    private String teacherId;
    private String teacherName;
    private String studentId;
    private String studentName;
    private String classId;
    private String className;
    private String courseName;
    private boolean isStudentPresent;

    public Attendance(){}

    public Attendance(String attendanceId, String teacherId, String teacherName, String studentId, String studentName, String classId, String className, String courseName, boolean isStudentPresent){
        this.attendanceId = attendanceId;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.classId = classId;
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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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
