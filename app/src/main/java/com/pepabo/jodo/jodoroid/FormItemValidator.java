package com.pepabo.jodo.jodoroid;

import android.content.Context;

public abstract class FormItemValidator {
    protected String  mError = "";
    protected Context mContext;

    public FormItemValidator(Context context) {
        mContext = context;
    }

    public abstract void validate(String formItem);

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
