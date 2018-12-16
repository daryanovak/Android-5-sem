package com.example.darya.myapplication.data.models;

public class RssFeedModel {
    public String title;
    public String link;
    public String description;
    public String date;
    public String srcPicture;

    public RssFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public RssFeedModel(String title, String link, String description, String date){
        this(title, link, description);
        this.date = date;
    }
}
