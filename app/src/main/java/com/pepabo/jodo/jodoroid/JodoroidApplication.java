package com.pepabo.jodo.jodoroid;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


import com.pepabo.jodo.jodoroid.models.APIService;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class JodoroidApplication extends Application {
    public static final String ENDPOINT = "https://157.7.190.186/api/";

    APIService mService;
    Picasso mPicasso;
    private static OkHttpClient mHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            mHttpClient = getOkHttpClient();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        mPicasso = createPicasso(this, mHttpClient);
        mService = createAPIService(this, mHttpClient);
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

    private static CookieHandler createCookieHandler() {
        return new CookieManager(); // TODO: Implement persistent cookie jar
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

    @NonNull
    private static OkHttpClient getOkHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new X509TrustManager[]{new TrustEveryoneX509TrustManager()}, null);

        final OkHttpClient client = new OkHttpClient();
        client.setCookieHandler(createCookieHandler());
        client.setSslSocketFactory(sslContext.getSocketFactory()); // XXX
        client.setHostnameVerifier(new NonVerifyingHostnameVerifier()); // XXX
        return client;
    }

    private static class TrustEveryoneX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            throw new CertificateException();
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // Trust any certificate!!!
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class NonVerifyingHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
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
            final Account account = getAccount(mContext);
            if(account == null) {
                return null;
            }

            final AccountManager accountManager = AccountManager.get(mContext);
            return accountManager.peekAuthToken(account, JodoAuthenticator.ACCOUNT_TOKEN_TYPE);
        }
    }

    static Account getAccount(Context context) {
        final AccountManager accountManager = AccountManager.get(context);
        final Account[] accounts = accountManager.getAccountsByType(JodoAuthenticator.ACCOUNT_TYPE);

        if(accounts.length == 0) {
            return null;
        }
        return accounts[0];
    }
}
