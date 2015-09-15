package com.pepabo.jodo.jodoroid.models;

public class Stardom {
    private boolean active;
    private String starStatus;

    public Stardom() {}

    public Stardom(boolean active, String star_status) {
        this.active     = active;
        this.starStatus = star_status;
    }

    public boolean isActive() {
        return this.active;
    }

    public String getStarStatus() {
        return this.starStatus;
    }
}
