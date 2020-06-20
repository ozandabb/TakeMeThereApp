package com.example.takemethereapp;

import java.sql.Timestamp;

public class BlogPost  {

    public String description, image_url, user_id;
    public Timestamp timestamp;

    public BlogPost() {
    }

    public BlogPost(String description, String image_url, String user_id, Timestamp timestamp) {
        this.description = description;
        this.image_url = image_url;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
