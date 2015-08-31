package com.pepabo.jodo.jodoroid;

import android.content.Context;

public abstract class FormItemValidator {
    protected String mError = "";

    public abstract void validate(Context context);

    public boolean hasError() {
        return !(mError.equals(""));
    }

    public String getErrorMessage() {
        return mError;
    }

    protected void resetError() {
        mError = "";
    }
}
