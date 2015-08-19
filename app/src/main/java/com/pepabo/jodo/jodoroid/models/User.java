package com.pepabo.jodo.jodoroid.models;

import java.net.URI;

public class User {
    long id;
    String name;
    URI avatar;

    public User() {
    }

    public User(long id, String name, URI avatar) {
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

    public URI getAvatar() {
        return avatar;
    }
}
