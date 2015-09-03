package com.pepabo.jodo.jodoroid;

public interface RefreshableView<Model> {
    boolean isRefreshing();
    void setRefreshing(boolean refreshing);

    void onNextModel(Model model);
    void onLoadError(Throwable e);
}
