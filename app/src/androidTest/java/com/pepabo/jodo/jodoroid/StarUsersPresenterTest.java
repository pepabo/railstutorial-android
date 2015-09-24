package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static org.mockito.Mockito.*;

public class StarUsersPresenterTest extends AndroidTestCase {

    APIService mAPIService;
    StarUsersPresenter mPresenter;
    RefreshableView<List<User>> mView;
    ExpirationManager mExpirationManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mAPIService = mock(APIService.class);
        mView = mock(RefreshableView.class);
        mExpirationManager = new ExpirationManager();
        mPresenter = new StarUsersPresenter(mAPIService, mExpirationManager);
        mPresenter.setView(mView);
    }

    public void testRefreshSuccess() {
        final List<User> users = new ArrayList<User>();

        when(mAPIService.fetchStarUsers(1)).thenReturn(
                Observable.create(new Observable.OnSubscribe<List<User>>() {
                    @Override
                    public void call(Subscriber<? super List<User>> subscriber) {
                        subscriber.onNext(users);
                        subscriber.onCompleted();
                    }
                })
        );

        mPresenter.refresh();

        verify(mAPIService).fetchStarUsers(1);
        verifyNoMoreInteractions(mAPIService);

        verify(mView).onNextModel(users);
        verify(mView).setRefreshing(false);
        verifyNoMoreInteractions(mView);
    }

    public void testRefreshFailure() {
        final Throwable error = new Error();

        when(mAPIService.fetchStarUsers(1)).thenReturn(
                Observable.create(new Observable.OnSubscribe<List<User>>() {
                    @Override
                    public void call(Subscriber<? super List<User>> subscriber) {
                        subscriber.onError(error);
                    }
                })
        );

        mPresenter.refresh();

        verify(mAPIService).fetchStarUsers(1);
        verifyNoMoreInteractions(mAPIService);

        verify(mView).onLoadError(error);
        verify(mView).setRefreshing(false);
        verifyNoMoreInteractions(mView);
    }
}
