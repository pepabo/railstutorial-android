package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator extends FormItemValidator {
    static Pattern REGEXP = Pattern.compile("\\A[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\\z");

    public EmailValidator(Context context) {
        super(context);
    }

    public void validate(String email) {
        resetError();

        if (TextUtils.isEmpty(email)) {
            mError = mContext.getString(R.string.error_field_required);
        } else {
            Matcher matcher = REGEXP.matcher(email);
            if (!matcher.matches()) {
                mError = mContext.getString(R.string.error_invalid_email);
            }
        }
    }
}
