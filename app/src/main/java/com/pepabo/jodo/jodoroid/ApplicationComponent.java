package com.pepabo.jodo.jodoroid;

import android.content.SharedPreferences;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.modules.APIModule;
import com.pepabo.jodo.jodoroid.modules.AndroidModule;
import com.pepabo.jodo.jodoroid.modules.HttpModule;
import com.pepabo.jodo.jodoroid.modules.PicassoModule;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AndroidModule.class, APIModule.class, HttpModule.class, PicassoModule.class})
public interface ApplicationComponent {
    Picasso picasso();
    APIService apiService();
    SharedPreferences sharedPreferences();
}
