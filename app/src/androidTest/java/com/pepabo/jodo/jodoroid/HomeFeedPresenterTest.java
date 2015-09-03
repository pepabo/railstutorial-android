package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;


import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.*;

public class HomeFeedPresenterTest extends AndroidTestCase {
    APIService mAPIService;
    HomeFeedPresenter mPresenter;
    RefreshableView<List<Micropost>> mView;
    ExpirationManager mExpirationManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();


        mAPIService = mock(APIService.class);
        mView = mock(RefreshableView.class);
        mExpirationManager = new ExpirationManager();
        mPresenter = new HomeFeedPresenter(mAPIService, mExpirationManager);
        mPresenter.setView(mView);
    }

    public void testRefresh() {
        final List<Micropost> feed = new ArrayList<>();

        when(mAPIService.fetchFeed(1)).thenReturn(
                Observable.create(new Observable.OnSubscribe<List<Micropost>>() {
                    @Override
                    public void call(Subscriber<? super List<Micropost>> subscriber) {
                        subscriber.onNext(feed);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchFeed(1);
        verify(mView, timeout(1000)).onNextModel(feed);
        verify(mView, timeout(1000)).setRefreshing(false);
    }


    public void testRefreshError() {
        final Throwable e = new Error();

        when(mAPIService.fetchFeed(1)).thenReturn(
                Observable.create(new Observable.OnSubscribe<List<Micropost>>() {
                    @Override
                    public void call(Subscriber<? super List<Micropost>> subscriber) {
                        subscriber.onError(e);
                    }
                }).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchFeed(1);
        verify(mView, timeout(1000)).onLoadError(e);
        verify(mView, timeout(1000)).setRefreshing(false);
    }
}
