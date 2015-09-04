package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

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

    @Override
    public Observable<User> loadNextPage(int pageNumber) {
        return mAPIService.fetchUser(mUserId, pageNumber);
    }
}
