package com.pepabo.jodo.jodoroid;

public interface ResetPasswordRequestView {
    void setEmailError(String message);

    String getEmail();
    void setEmail(String value);

    void showProgress(boolean show);

    void onError(Throwable e);

    void setResult(int resultCode);

    void finish();
}
