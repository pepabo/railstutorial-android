package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.text.TextUtils;

public class NameValidator extends FormItemValidator {
    private String eName;

    public NameValidator(String name) {
        eName = name;
    }

    public void validate(Context context) {
        resetError();

        if (TextUtils.isEmpty(eName)) {
            mError = context.getString(R.string.error_field_required);
        }
    }
}
