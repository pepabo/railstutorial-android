package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator extends FormItemValidator {
    static Pattern REGEXP = Pattern.compile("\\A[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\\z");

    private String mEmail;

    public EmailValidator(String email) {
        mEmail = email;
    }

    public void validate(Context context) {
        mError = "";

        if (TextUtils.isEmpty(mEmail)) {
            mError = context.getString(R.string.error_field_required);
        } else {
            Matcher matcher = REGEXP.matcher(mEmail);
            if (!matcher.matches()) {
                mError = context.getString(R.string.error_invalid_email);
            }
        }
    }
}
