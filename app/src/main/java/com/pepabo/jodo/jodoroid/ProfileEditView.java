package com.pepabo.jodo.jodoroid;

public interface ProfileEditView {
    void setNameError(String message);
    void setEmailError(String message);

    String getEmail();
    void setEmail(String value);
    String getName();
    void setName(String value);

    void showProgress(boolean show);

    void finish();
}
