package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.*;

public class AllUsersPresenterTest extends AndroidTestCase{
    APIService mAPIService;
    AllUsersPresenter mPresenter;
    RefreshableView<List<User>> mView;
    ExpirationManager mExpirationManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mAPIService = mock(APIService.class);
        mView = mock(RefreshableView.class);
        mExpirationManager = new ExpirationManager();
        mPresenter = new AllUsersPresenter(mAPIService, mExpirationManager);
        mPresenter.setView(mView);
    }

    public void testRefresh() {
        final List<User> users = new ArrayList<>();

        when(mAPIService.fetchAllUsers(1)).thenReturn(
                Observable.create(new Observable.OnSubscribe<List<User>>() {
                    @Override
                    public void call(Subscriber<? super List<User>> subscriber) {
                        subscriber.onNext(users);
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchAllUsers(1);
        verify(mView, timeout(1000)).onNextModel(users);
        verify(mView, timeout(1000)).setRefreshing(false);
    }

    public void testRefreshError() {
        final Throwable e = new Error();

        when(mAPIService.fetchAllUsers(1)).thenReturn(
                Observable.create(new Observable.OnSubscribe<List<User>>() {
                    @Override
                    public void call(Subscriber<? super List<User>> subscriber) {
                        subscriber.onError(e);
                    }
                }).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchAllUsers(1);
        verify(mView, timeout(1000)).onLoadError(e);
        verify(mView, timeout(1000)).setRefreshing(false);
    }
}
