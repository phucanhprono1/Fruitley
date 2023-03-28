package com.example.fruitley.model;

import java.io.Serializable;

public class User implements Serializable {
    private String username,phoneNumber,password;
    public User(String username, String phoneNumber, String password){
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


}
