package com.pepabo.jodo.jodoroid.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;

@Module
public class HttpModule {
    private static final String CACHE_DIR_NAME = "http-cache";

    @NonNull
    @Provides
    public CookieHandler provideCookieHandler() {
        return new CookieManager(); // TODO: Implement persistent cookie jar
    }

    @Provides
    public OkHttpClient provideHttpClient(CookieHandler cookieHandler, Cache cache,
                                          LocaleInterceptor localeInterceptor,
                                          X509TrustManager[] trustManagers,
                                          HostnameVerifier hostnameVerifier) {
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);

            final OkHttpClient client = new OkHttpClient();
            client.setCookieHandler(cookieHandler);
            client.setSslSocketFactory(sslContext.getSocketFactory());
            client.setHostnameVerifier(hostnameVerifier);
            client.setCache(cache);
            client.interceptors().add(localeInterceptor);
            return client;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            return null;
        }
    }

    @NonNull
    @Provides
    public X509TrustManager[] provideTrustManagers() {
        return new X509TrustManager[]{new TrustEveryoneX509TrustManager()}; // XXX
    }

    @NonNull
    @Provides
    public HostnameVerifier provideHostnameVerifier() {
        return new NonVerifyingHostnameVerifier(); // XXX
    }

    @NonNull
    @Provides
    public Cache provideCache(Context context) {
        final File cacheDir = new File(context.getCacheDir(), CACHE_DIR_NAME);
        return new Cache(cacheDir, 20 * 1024 * 1024);
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

    public static class LocaleInterceptor implements Interceptor {
        private final static String HTTP_ACCEPT_LANGUAGE = "Accept-Language";

        final String mLanguageTag;

        @Inject
        public LocaleInterceptor() {
            mLanguageTag = makeLanguageTag(Locale.getDefault());
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request().newBuilder()
                    .addHeader(HTTP_ACCEPT_LANGUAGE, mLanguageTag)
                    .build();
            return chain.proceed(request);
        }

        static String makeLanguageTag(Locale locale) {
            final String language = locale.getLanguage(),
                    country = locale.getCountry(),
                    variant = locale.getVariant();

            final StringBuilder builder = new StringBuilder(language);
            if (!country.isEmpty()) {
                builder.append('-').append(country);
            }
            if (!variant.isEmpty()) {
                builder.append('-').append(variant.replace('-', '_'));
            }

            return builder.toString();
        }
    }
}
