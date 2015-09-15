package com.pepabo.jodo.jodoroid.models;

import android.net.Uri;

import java.util.Date;

public class Micropost implements Comparable<Micropost> {
    long id;
    String content;
    User user;
    Date createdAt;
    Uri pictureUrl;
    boolean starred;

    public Micropost() {
    }

    public Micropost(long id, String content, User user, Date createdAt, Uri pictureUrl, boolean starred) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
        this.pictureUrl = pictureUrl;
        this.starred = starred;
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

    public Uri getPictureUrl() {
        return pictureUrl;
    }

    public boolean isStarred() {
        return starred;
    }

    @Override
    public int compareTo(Micropost another) {
        return getCreatedAt().compareTo(another.getCreatedAt());
    }
}
