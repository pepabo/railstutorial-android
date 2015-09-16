package com.pepabo.jodo.jodoroid.modules;

import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {
    Application mApplication;

    public AndroidModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public AccountManager provideAccountManager() {
        return AccountManager.get(mApplication);
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApplication);
    }
}
