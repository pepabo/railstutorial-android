package com.pepabo.jodo.jodoroid;

public class ExpirationManager {
    long mExpired;

    public ExpirationManager() {
        mExpired = getCurrentTimestamp();
    }

    public long getCurrentTimestamp()  {
        return System.nanoTime();
    }

    public void expire() {
        mExpired = getCurrentTimestamp();
    }

    public boolean isExpired(long timestamp) {
        return timestamp <= mExpired;
    }
}
