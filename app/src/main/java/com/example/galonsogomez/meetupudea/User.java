package com.example.galonsogomez.meetupudea;

public class User {

    private String userUID;

    public User(String userUID) {
        this.userUID = userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
