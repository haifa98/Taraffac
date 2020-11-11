package com.example.taraffac;

public class User {
    private String username;
    private String email;
    private String password;
    private String addingType;
    private String Display_Option;
    private String voice_command;

    public User() {


    }
    public User(String username, String email, String password,  String addingType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.addingType = addingType;

    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getAddingType() {
        return addingType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setAddingType(String addingType) {
        this.addingType = addingType;
    }
}
