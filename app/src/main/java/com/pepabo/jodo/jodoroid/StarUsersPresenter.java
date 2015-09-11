package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

import rx.Observable;

public class StarUsersPresenter extends RefreshPresenter<List<User>> {

    APIService mAPIService;
    
    public StarUsersPresenter(APIService apiService) {
        super();
        mAPIService = apiService;
    }

    @Override
    protected Observable<List<User>> getObservable() {
        return mAPIService.fetchAllUsers(1);
    }

    @Override
    protected Observable<List<User>> loadNextPage(int pageNumber) {
        return mAPIService.fetchAllUsers(pageNumber);
    }
}
