package com.pepabo.jodo.jodoroid.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pepabo.jodo.jodoroid.BuildConfig;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class PicassoModule {
    @NonNull
    @Provides
    public Picasso providePicasso(Context context, OkHttpClient httpClient) {
        return new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(httpClient))
                .build();
    }
}
