package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import rx.Observable;

public class UserProfilePresenter extends RefreshPresenter<User> {
    APIService mAPIService;
    long mUserId;

    public UserProfilePresenter(RefreshableView<User> view, APIService apiService, long userId) {
        super(view);
        mAPIService = apiService;
        mUserId = userId;
    }

    @Override
    protected Observable<User> getObservable() {
        return mAPIService.fetchUser(mUserId, 1);
    }
}
