package com.example.galonsogomez.meetupudea;

import android.provider.MediaStore;

import java.io.File;
import java.util.Date;

public class Group {

    private String groupUID;
    private String userUID;
    private String title;
    private String picture;
    private String description;

    public Group() {

    }

    public Group(String groupUID, String userUID, String title, String picture, String description) {
        this.groupUID = groupUID;
        this.userUID = userUID;
        this.title = title;
        this.picture = picture;
        this.description = description;
    }

    public Group(String groupUID, String title, String picture, String description) {
        this.groupUID = groupUID;
        this.title = title;
        this.picture = picture;
        this.description = description;
    }

    public Group(String groupUID) {
        this.groupUID = groupUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupUID() {
        return groupUID;
    }

    public void setGroupUID(String groupUID) {
        this.groupUID = groupUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
