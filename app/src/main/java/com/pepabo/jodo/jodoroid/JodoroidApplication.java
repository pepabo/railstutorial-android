package com.pepabo.jodo.jodoroid;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.Picasso;


import com.pepabo.jodo.jodoroid.models.APIService;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

public class JodoroidApplication extends Application {
    public static final String ENDPOINT = "https://157.7.190.186/api/";

    APIService mService;
    Picasso mPicasso;

    @Override
    public void onCreate() {
        super.onCreate();
        mPicasso = createPicasso(getApplicationContext());
        mService = createAPIService();
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    public APIService getAPIService() {
        return mService;
    }

    private static Picasso createPicasso(Context context) {
        return new Picasso.Builder(context)
                .loggingEnabled(true)
                .indicatorsEnabled(BuildConfig.DEBUG)
                .build();
    }

    private static CookieHandler createCookieHandler() {
        return new CookieManager(); // TODO: Implement persistent cookie jar
    }

    private static APIService createAPIService() {
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new X509TrustManager[]{new TrustEveryoneX509TrustManager()}, null);

            final OkHttpClient client = new OkHttpClient();
            client.setCookieHandler(createCookieHandler());
            client.setSslSocketFactory(sslContext.getSocketFactory()); // XXX
            client.setHostnameVerifier(new NonVerifyingHostnameVerifier()); // XXX

            final RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .setClient(new OkClient(client))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new AndroidLog("API"))
                    .build();

            return adapter.create(APIService.class);

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
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
}
