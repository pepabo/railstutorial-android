package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private String mPassword;
    private String mError = "";

    public PasswordValidator(String password) {
        mPassword = password;
    }

    public void validate(Context context) {
        mError = "";

        if (TextUtils.isEmpty(mPassword)) {
            mError = context.getString(R.string.error_field_required);
        } else if (mPassword.length() < 6) {
            mError = context.getString(R.string.error_invalid_email);
        }
    }

    public boolean hasError() {
        return !TextUtils.isEmpty(mError);
    }

    public String getErrorMessage() {
        return mError;
    }
}
