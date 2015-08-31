package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.text.TextUtils;

public class PasswordValidator extends FormItemValidator {
    public PasswordValidator(Context context) {
        super(context);
    }

    public void validate(String password) {
        resetError();

        if (TextUtils.isEmpty(password)) {
            mError = mContext.getString(R.string.error_field_required);
        } else if (password.length() < 6) {
            mError = mContext.getString(R.string.error_invalid_email);
        }
    }
}
