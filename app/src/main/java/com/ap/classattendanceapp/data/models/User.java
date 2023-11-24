package com.ap.classattendanceapp.data.models;

import java.util.List;

public class User {
    private String userId;
    private String userEmail;
    private String userPassword;
    private String name;
    private String surname;
    private String userType;
    private List<String> courses;
    private List<String> classes;
    private List<String> attendedClasses;
}
