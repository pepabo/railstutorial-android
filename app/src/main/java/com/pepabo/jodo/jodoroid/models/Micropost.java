package com.pepabo.jodo.jodoroid.models;

import java.net.URI;
import java.util.Date;

public class Micropost {
    long id;
    String content;
    User user;
    Date createdAt;
    URI picture;

    public Micropost() {
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public URI getPicture() {
        return picture;
    }
}
