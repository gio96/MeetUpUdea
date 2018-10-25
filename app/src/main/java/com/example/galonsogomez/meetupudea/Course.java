package com.example.galonsogomez.meetupudea;

/**
 * Created by personal on 24/10/18.
 */

public class Course {

    private String courseUID;
    private String title;
    private String place;
    private String schedule;
    private String picture;
    private String description;

    public Course() {
    }

    public Course(String courseUID, String title, String place, String schedule, String picture, String description) {
        this.courseUID = courseUID;
        this.title = title;
        this.place = place;
        this.schedule = schedule;
        this.picture = picture;
        this.description = description;
    }

    public String getCourseUID() {
        return courseUID;
    }

    public void setCourseUID(String courseUID) {
        this.courseUID = courseUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
