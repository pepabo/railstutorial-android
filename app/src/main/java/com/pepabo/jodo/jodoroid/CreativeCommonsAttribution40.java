package com.pepabo.jodo.jodoroid;

import android.content.Context;

import de.psdev.licensesdialog.licenses.License;

public class CreativeCommonsAttribution40 extends License {
    @Override
    public String getName() {
        return "Creative Common Attribution 4.0 International License";
    }

    @Override
    public String readSummaryTextFromResources(Context context) {
        return getContent(context, R.raw.ccby_40_summary);
    }

    @Override
    public String readFullTextFromResources(Context context) {
        return getContent(context, R.raw.ccby_40_full);
    }

    @Override
    public String getVersion() {
        return "4.0";
    }

    @Override
    public String getUrl() {
        return "https://creativecommons.org/licenses/by/4.0/";
    }
}
