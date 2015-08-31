package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.text.TextUtils;

public abstract class FormItemValidator {
    protected String mError = "";

    public abstract void validate(Context context);

    public boolean hasError() {
        return !TextUtils.isEmpty(mError);
    }

    public String getErrorMessage() {
        return mError;
    }
}
