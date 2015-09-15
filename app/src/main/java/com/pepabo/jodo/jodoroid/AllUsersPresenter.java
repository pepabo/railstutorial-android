package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

import rx.Observable;

public class AllUsersPresenter extends RefreshPresenter<List<User>> {

    APIService mAPIService;

    public AllUsersPresenter(APIService apiService) {
        super();
        mAPIService = apiService;
    }

    @Override
    protected Observable<List<User>> getObservable(int page) {
        return mAPIService.fetchAllUsers(page);
    }
}
