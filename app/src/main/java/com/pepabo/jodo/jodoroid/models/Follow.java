package com.pepabo.jodo.jodoroid.models;


public class Follow {
    boolean following;
    boolean followed;

    public Follow() {
    }

    public Follow(boolean following, boolean followed) {
        this.following = following;
        this.followed = followed;
    }

    public boolean getFollowing() { return this.following; }

    public boolean getFollowed() { return this.followed; }

}
