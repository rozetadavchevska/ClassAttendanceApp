package com.ap.classattendanceapp.data.models;

import java.util.List;

public class User {
    private String userId;
    private String userEmail;
    private String userPassword;
    private String firstName;
    private String lastName;
    private String userType;
    private List<String> coursesId;
    private List<String> classes;
    private List<String> attendedClasses;

    public User(){}

    public User(String userId, String userEmail, String userPassword, String firstName, String lastName,
                String userType, List<String> coursesId, List<String> classes, List<String> attendedClasses){
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.coursesId = coursesId;
        this.classes = classes;
        this.attendedClasses = attendedClasses;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return firstName;
    }

    public void setUserFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<String> getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(List<String> courses) {
        this.coursesId = coursesId;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public List<String> getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(List<String> attendedClasses) {
        this.attendedClasses = attendedClasses;
    }
}
