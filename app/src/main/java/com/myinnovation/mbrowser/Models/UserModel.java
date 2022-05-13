package com.myinnovation.mbrowser.Models;

public class UserModel {
    String username, email;

    public UserModel(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public UserModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
