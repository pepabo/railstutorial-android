package com.pepabo.jodo.jodoroid;

import android.app.Activity;

public interface ResetPasswordView {
    void setPasswordError(String message);

    String getPassword();
    void setPassword(String value);

    String getToken();

    String getEmail();

    Activity getActivity();

    JodoroidApplication getJodoroidApplication();

    void showProgress(boolean show);

    void onError(Throwable e);
    void onSuccess();

    void finish();
}
