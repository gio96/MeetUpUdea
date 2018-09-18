package com.example.galonsogomez.meetupudea;

public class User {

    private String userUID;
    private String name;
    private String following;

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public User(String userUID) {
        this.userUID = userUID;
    }

    public User() {
    }
}
