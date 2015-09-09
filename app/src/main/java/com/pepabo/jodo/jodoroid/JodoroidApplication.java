package com.pepabo.jodo.jodoroid;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.pepabo.jodo.jodoroid.modules.APIModule;
import com.pepabo.jodo.jodoroid.modules.HttpModule;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


import com.pepabo.jodo.jodoroid.models.APIService;
import com.squareup.okhttp.OkHttpClient;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class JodoroidApplication extends Application implements OnAccountsUpdateListener {

    public static final String ACTION_LOGGED_OUT = "com.pepabo.jodo.jodoroid.LOGGED_OUT";

    APIService mService;
    Picasso mPicasso;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            final OkHttpClient httpClient = HttpModule.provideHttpClient(this);
            mPicasso = createPicasso(this, httpClient);
            mService = APIModule.createAPIService(this, httpClient);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        AccountManager.get(this).addOnAccountsUpdatedListener(this, null, true);
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    public APIService getAPIService() {
        return mService;
    }

    private static Picasso createPicasso(Context context, OkHttpClient httpClient) {
        return new Picasso.Builder(context)
                .loggingEnabled(true)
                .downloader(new OkHttpDownloader(httpClient))
                .indicatorsEnabled(BuildConfig.DEBUG)
                .build();
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        boolean found = false;

        for (Account account : accounts) {
            if(account.type.equals(JodoAuthenticator.ACCOUNT_TYPE)) {
                found = true;
            }
        }

        if(!found) {
            sendBroadcast(new Intent(ACTION_LOGGED_OUT));
        }
    }

    public static IntentFilter createLoggedOutIntentFilter() {
        return new IntentFilter(ACTION_LOGGED_OUT);
    }
}
