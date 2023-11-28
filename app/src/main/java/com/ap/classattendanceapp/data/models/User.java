package com.ap.classattendanceapp.data.models;

import java.util.List;
import java.util.Map;

public class User {
    private String userId;
    private String userEmail;
    private String userPassword;
    private String firstName;
    private String lastName;
    private String userType;
    private Map<String, Boolean> coursesId;
    private Map<String, Boolean> classesIds;
    private List<String> attendedClasses;

    public User(){}

    public User(String userId, String userEmail, String userPassword, String firstName, String lastName,
                String userType, Map<String, Boolean> coursesId, Map<String, Boolean> classesIds, List<String> attendedClasses){
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.coursesId = coursesId;
        this.classesIds = classesIds;
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

    public Map<String, Boolean> getCoursesId() {
        return coursesId;
    }

    public void setClassesIds(Map<String, Boolean> classesIds) { this.classesIds = classesIds;}

    public Map<String, Boolean> getClassesIds() { return classesIds; }

    public void setCoursesId(Map<String, Boolean> coursesId) {this.coursesId = coursesId;}

    public List<String> getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(List<String> attendedClasses) {
        this.attendedClasses = attendedClasses;
    }
}
