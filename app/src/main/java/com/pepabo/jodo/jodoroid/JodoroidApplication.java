package com.pepabo.jodo.jodoroid;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.Picasso;

public class JodoroidApplication extends Application {
    Picasso mPicasso;

    @Override
    public void onCreate() {
        super.onCreate();
        mPicasso = createPicasso(getApplicationContext());
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    private static Picasso createPicasso(Context context) {
        return new Picasso.Builder(context)
                .loggingEnabled(true)
                .indicatorsEnabled(BuildConfig.DEBUG)
                .build();
    }
}
