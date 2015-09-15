package com.pepabo.jodo.jodoroid.models;

public class Stardom {
    private final String CANDIDATE = "candidate";
    private final String ACCEPTED  = "accepted";
    private final String DECLIEND  = "declined";

    private boolean active     = false;
    private String  starStatus = null;

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

    public boolean isCandidate() {
        return (this.starStatus != null) && this.starStatus.equals(CANDIDATE);
    }

    public boolean isAccepted() {
        return (this.starStatus != null) && this.starStatus.equals(ACCEPTED);
    }

    public boolean isDeclined() {
        return (this.starStatus != null) && this.starStatus.equals(DECLIEND);
    }
}
