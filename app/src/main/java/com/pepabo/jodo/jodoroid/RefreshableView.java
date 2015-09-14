package com.pepabo.jodo.jodoroid;

import android.app.ListFragment;

public interface RefreshableView<Model> {
    boolean isRefreshing();
    void setRefreshing(boolean refreshing);

    void onNextModel(Model model);
    void onLoadError(Throwable e);

    void onMoreModel(Model model);

    ListFragment getFragment();

}
