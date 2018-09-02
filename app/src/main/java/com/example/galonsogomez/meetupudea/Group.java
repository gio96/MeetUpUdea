package com.example.galonsogomez.meetupudea;

import android.provider.MediaStore;

import java.io.File;
import java.util.Date;

public class Group {

    //private String idGroup;
    private String Title;
    private int Thumbnail;
    private String Picture;
    /*private String description;
    private Date date;
    private String location;
    private Integer numberAssistans;
    private Date finishDate;*/

    public Group(String title, String picture) {
        this.Title = title;
        this.Picture = picture;
    }

    public Group(String title) {
        this.Title = title;
    }

    public Group() {

    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        this.Picture = picture;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.Thumbnail = thumbnail;
    }
}
