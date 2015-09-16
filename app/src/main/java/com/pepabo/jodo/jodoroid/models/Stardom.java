package com.pepabo.jodo.jodoroid.models;

public class Stardom {
    private final String CANDIDATE = "candidate";
    private final String ACCEPTED = "accepted";
    private final String DECLINED = "declined";

    private boolean active = false;
    private String starStatus = null;
    private String date;

    public Stardom() {
    }

    public Stardom(boolean active, String starStatus, String date) {
        this.active = active;
        this.starStatus = starStatus;
        this.date = date;
    }

    public boolean isActive() {
        return this.active;
    }

    public String getStarStatus() {
        return this.starStatus;
    }

    public String getDate() {
        return this.date;
    }

    public boolean isCandidate() {
        return (this.starStatus != null) && this.starStatus.equals(CANDIDATE);
    }

    public boolean isAccepted() {
        return (this.starStatus != null) && this.starStatus.equals(ACCEPTED);
    }

    public boolean isDeclined() {
        return (this.starStatus != null) && this.starStatus.equals(DECLINED);
    }
}
