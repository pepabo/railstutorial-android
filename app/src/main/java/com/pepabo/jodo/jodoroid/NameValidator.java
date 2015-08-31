package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator {
    private String eName;
    private String mError = "";

    public NameValidator(String name) {
        eName = name;
    }

    public void validate(Context context) {
        mError = "";

        if (TextUtils.isEmpty(eName)) {
            mError = context.getString(R.string.error_field_required);
        }
    }

    public boolean hasError() {
        return !TextUtils.isEmpty(mError);
    }

    public String getErrorMessage() {
        return mError;
    }
}
