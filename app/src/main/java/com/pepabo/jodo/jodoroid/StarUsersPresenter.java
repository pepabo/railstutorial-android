package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

import rx.Observable;

public class StarUsersPresenter extends RefreshPresenter<List<User>> {

    APIService mAPIService;

    public StarUsersPresenter(APIService apiService, ExpirationManager expirationManager) {
        super(expirationManager);
        mAPIService = apiService;
    }

    @Override
    protected Observable<List<User>> getObservable(int page) {
        return mAPIService.fetchStarUsers(page);
    }

    @Override
    public boolean isLast(List<User> users) {
        return users.size() == 0;
    }
}
