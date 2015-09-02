package com.pepabo.jodo.jodoroid.models;

public class Session {
    private String authToken;
    private User user;

    public Session() {
    }

    public String getAuthToken() {
        return authToken;
    }

    public User getUser() {
        return user;
    }
}
