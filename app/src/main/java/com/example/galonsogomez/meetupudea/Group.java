package com.example.galonsogomez.meetupudea;

import android.provider.MediaStore;

import java.io.File;
import java.util.Date;

public class Group {

    //private String idGroup;
    private String Title;
    private int Thumbnail;
    /*private String description;
    private Date date;
    private String location;
    private Integer numberAssistans;
    private Date finishDate;*/

    public Group(String title, int thumbnail) {
        Title = title;
        Thumbnail = thumbnail;
    }

    public Group() {

    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }
}
