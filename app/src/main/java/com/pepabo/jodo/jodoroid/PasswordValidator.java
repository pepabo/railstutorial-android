package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.text.TextUtils;

public class PasswordValidator extends FormItemValidator {
    private String mPassword;

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
}
