package com.pepabo.jodo.jodoroid.models;

import android.net.Uri;

public class User {
    long id;
    String name;
    Uri avatar;

    public User() {
    }

    public User(long id, String name, Uri avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Uri getAvatar() {
        return avatar;
    }
}
