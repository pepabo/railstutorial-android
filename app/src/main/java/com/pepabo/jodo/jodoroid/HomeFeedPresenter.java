package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;

import java.util.List;

import rx.Observable;

public class HomeFeedPresenter extends RefreshPresenter<List<Micropost>> {
    APIService mAPIService;

    public HomeFeedPresenter(APIService apiService) {
        super();
        mAPIService = apiService;
    }

    @Override
    protected Observable<List<Micropost>> getObservable(int page) {
        return mAPIService.fetchFeed(page);
    }
}
