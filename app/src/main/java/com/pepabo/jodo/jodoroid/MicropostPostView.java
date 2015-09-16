package com.pepabo.jodo.jodoroid;

import retrofit.mime.TypedFile;

public interface MicropostPostView {
    String getContent();
    TypedFile getAttachment();

    void showProgress(boolean show);

    void onError(Throwable e);
    void finish();
}
