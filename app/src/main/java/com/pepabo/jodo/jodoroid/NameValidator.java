package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.text.TextUtils;

public class NameValidator extends FormItemValidator {

    public NameValidator(Context context) {
        super(context);
    }

    public void validate(String name) {
        resetError();

        if (TextUtils.isEmpty(name)) {
            mError = mContext.getString(R.string.error_field_required);
        }
    }
}
