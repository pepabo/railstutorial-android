package com.pepabo.jodo.jodoroid.modules;

import com.pepabo.jodo.jodoroid.ExpirationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ExpirationModule {
    @Provides
    @Singleton
    ExpirationManager provideExpirationManager() {
        return new ExpirationManager();
    }
}
