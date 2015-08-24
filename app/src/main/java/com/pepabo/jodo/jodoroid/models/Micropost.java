package com.pepabo.jodo.jodoroid.models;

import java.net.URI;
import java.util.Date;

public class Micropost implements Comparable<Micropost> {
    long id;
    String content;
    User user;
    Date createdAt;
    URI picture;

    public Micropost() {
    }

    public Micropost(long id, String content, User user, Date createdAt, URI picture) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
        this.picture = picture;
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

    @Override
    public int compareTo(Micropost another) {
        return getCreatedAt().compareTo(another.getCreatedAt());
    }
}
