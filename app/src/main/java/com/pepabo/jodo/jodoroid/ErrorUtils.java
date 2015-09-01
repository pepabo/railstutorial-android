package com.pepabo.jodo.jodoroid;

import android.text.TextUtils;

import com.pepabo.jodo.jodoroid.models.Failure;

import retrofit.RetrofitError;

public class ErrorUtils {
    static String getMessage(final Throwable e) {
        String message = null;

        if (e instanceof RetrofitError) {
            try {
                final Failure failure = (Failure) ((RetrofitError) e).getBodyAs(Failure.class);
                message = TextUtils.join(System.getProperty("line.separator"), failure.getErrors());
            } catch (RuntimeException re) {
                // ignore deserialization error
            }
        }

        if (message == null) {
            message = e.getLocalizedMessage();
        }

        return message;
    }
}
