package com.pepabo.jodo.jodoroid;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pepabo.jodo.jodoroid.modules.HttpModule;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


import com.pepabo.jodo.jodoroid.models.APIService;
import com.squareup.okhttp.OkHttpClient;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class JodoroidApplication extends Application implements OnAccountsUpdateListener {
    public static final String ENDPOINT = "https://157.7.190.186/api/";

    public static final String ACTION_LOGGED_OUT = "com.pepabo.jodo.jodoroid.LOGGED_OUT";

    APIService mService;
    Picasso mPicasso;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            final OkHttpClient httpClient = HttpModule.provideHttpClient(this);
            mPicasso = createPicasso(this, httpClient);
            mService = createAPIService(this, httpClient);
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

    private static Gson createGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(Uri.class, new UriDeserializer())
                .create();
    }

    private static APIService createAPIService(Context context, OkHttpClient httpClient) {
        final RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(createGson()))
                .setClient(new OkClient(httpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("API"))
                .setRequestInterceptor(new AuthenticationInterceptor(context))
                .build();

        return adapter.create(APIService.class);
    }

    static class AuthenticationInterceptor implements RequestInterceptor {
        private final static String HTTP_AUTHORIZATION = "Authorization";

        final Context mContext;

        public AuthenticationInterceptor(Context context) {
            this.mContext = context;
        }

        @Override
        public void intercept(RequestFacade request) {
            final String authToken = getAuthToken();
            if (authToken != null) {
                request.addHeader(HTTP_AUTHORIZATION, authToken);
            }
        }

        private String getAuthToken() {
            final JodoAccount account = JodoAccount.getAccount(mContext);
            if (account == null) {
                return null;
            }

            return account.getAuthToken();
        }
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
