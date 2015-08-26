package com.pepabo.jodo.jodoroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class JodoAuthenticatorService extends Service {
    JodoAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();

        mAuthenticator = new JodoAuthenticator((JodoroidApplication) getApplication());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
