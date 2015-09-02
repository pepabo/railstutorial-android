package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;

import java.util.List;

import rx.Observable;

public class HomeFeedPresenter extends RefreshPresenter<List<Micropost>> {
    APIService mAPIService;

    public HomeFeedPresenter(RefreshableView<List<Micropost>> view, APIService apiService) {
        super(view);
        mAPIService = apiService;
    }

    @Override
    protected Observable<List<Micropost>> getObservable() {
        return mAPIService.fetchFeed(1);
    }
}
