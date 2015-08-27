package com.pepabo.jodo.jodoroid.models;

import android.net.Uri;

public class User {
    long id;
    String name;
    Uri avatarUrl;
    long followersCount;
    long followingCount;
    long micropostsCount;

    public User() {
    }

    public User(long id, String name, Uri avatarUrl, long followersCount, long followingCount, long micropostsCount) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.micropostsCount = micropostsCount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Uri getAvatarUrl() {
        return avatarUrl;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public long getMicropostsCount() {
        return micropostsCount;
    }
}
