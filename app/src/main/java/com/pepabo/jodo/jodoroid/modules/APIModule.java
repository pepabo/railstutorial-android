package com.pepabo.jodo.jodoroid.modules;

import android.accounts.AccountManager;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pepabo.jodo.jodoroid.DateDeserializer;
import com.pepabo.jodo.jodoroid.JodoAccount;
import com.pepabo.jodo.jodoroid.UriDeserializer;
import com.pepabo.jodo.jodoroid.models.APIService;
import com.squareup.okhttp.OkHttpClient;

import java.util.Date;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module
public class APIModule {
    public static final String ENDPOINT = "https://157.7.190.186/api/";

    @NonNull
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(Uri.class, new UriDeserializer())
                .create();
    }

    @NonNull
    @Provides
    public RestAdapter provideRestAdapter(OkHttpClient httpClient, Gson gson,
                                          AuthenticationInterceptor authenticationInterceptor) {
        return new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(httpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("API"))
                .setRequestInterceptor(authenticationInterceptor)
                .build();
    }

    @NonNull
    @Provides
    public APIService provideAPIService(RestAdapter adapter) {
        return adapter.create(APIService.class);
    }

    public static class AuthenticationInterceptor implements RequestInterceptor {
        private final static String HTTP_AUTHORIZATION = "Authorization";

        final AccountManager mAccountManager;

        @Inject
        public AuthenticationInterceptor(AccountManager accountManager) {
            this.mAccountManager = accountManager;
        }

        @Override
        public void intercept(RequestFacade request) {
            final String authToken = getAuthToken();
            if (authToken != null) {
                request.addHeader(HTTP_AUTHORIZATION, authToken);
            }
        }

        private String getAuthToken() {
            final JodoAccount account = JodoAccount.getAccount(mAccountManager);
            if (account == null) {
                return null;
            }

            return account.getAuthToken();
        }
    }
}
