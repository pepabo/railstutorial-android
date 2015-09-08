package com.pepabo.jodo.jodoroid;

public interface PasswordChangeView {
    void setPasswordError(String message);

    String getPassword();
    void setPassword(String value);

    void showProgress(boolean show);

    void onError(Throwable e);
    void finish();
}
