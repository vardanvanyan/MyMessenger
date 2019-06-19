package com.example.mymessenger.users;

public class User {
    private String name;
    private String surName;
    private String key;


    public User(String name, String surName, String key) {
        this.name = name;
        this.surName = surName;
        this.key = key;
    }

    public User() {

    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    String getSurName() {
        return surName;
    }

}
