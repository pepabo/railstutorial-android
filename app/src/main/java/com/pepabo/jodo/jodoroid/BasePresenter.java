package com.pepabo.jodo.jodoroid;

public class BasePresenter<T> {
    T mView;

    public BasePresenter() {
    }

    public T getView() {
        return mView;
    }

    public void setView(T view) {
        mView = view;
    }
}
