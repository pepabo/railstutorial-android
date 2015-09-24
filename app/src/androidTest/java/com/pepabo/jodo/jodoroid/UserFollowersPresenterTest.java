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

public class UserFollowersPresenterTest extends AndroidTestCase {
    APIService mAPIService;
    UserFollowersPresenter mPresenter;
    RefreshableView<List<User>> mView;
    ExpirationManager mExpirationManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mAPIService = mock(APIService.class);
        mView = mock(RefreshableView.class);
        mExpirationManager = new ExpirationManager();
    }

    public void testRefreshFollowersSuccess() {
        final List<User> users = new ArrayList<>();

        mPresenter = new UserFollowersPresenter(
                mAPIService, mExpirationManager, UserFollowersPresenter.TYPE_FOLLOWERS, 1
        );
        mPresenter.setView(mView);

        when(mAPIService.fetchFollowers(1, 1)).thenReturn(
                Observable.just(users).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchFollowers(1, 1);
        verifyNoMoreInteractions(mAPIService);

        verify(mView, timeout(1000)).onNextModel(users);
        verify(mView, timeout(1000)).setRefreshing(false);
        verifyNoMoreInteractions(mView);
    }

    public void testRefreshFollowersFailure() {
        final Throwable error = new Error();

        mPresenter = new UserFollowersPresenter(
                mAPIService, mExpirationManager, UserFollowersPresenter.TYPE_FOLLOWERS, 1
        );
        mPresenter.setView(mView);

        when(mAPIService.fetchFollowers(1, 1)).thenReturn(
                Observable.<List<User>>error(error).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchFollowers(1, 1);
        verifyNoMoreInteractions(mAPIService);

        verify(mView, timeout(1000)).onLoadError(error);
        verify(mView, timeout(1000)).setRefreshing(false);
        verifyNoMoreInteractions(mView);
    }


    public void testRefreshFollowingSuccess() {
        final List<User> users = new ArrayList<User>();

        mPresenter = new UserFollowersPresenter(
                mAPIService, mExpirationManager, UserFollowersPresenter.TYPE_FOLLOWING, 1
        );
        mPresenter.setView(mView);

        when(mAPIService.fetchFollowing(1, 1)).thenReturn(
                Observable.just(users).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchFollowing(1, 1);
        verifyNoMoreInteractions(mAPIService);

        verify(mView, timeout(1000)).onNextModel(users);
        verify(mView, timeout(1000)).setRefreshing(false);
        verifyNoMoreInteractions(mView);
    }

    public void testRefreshFollowingFailure() {
        final Throwable error = new Error();

        mPresenter = new UserFollowersPresenter(
                mAPIService, mExpirationManager, UserFollowersPresenter.TYPE_FOLLOWING, 1
        );
        mPresenter.setView(mView);

        when(mAPIService.fetchFollowing(1, 1)).thenReturn(
                Observable.<List<User>>error(error).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchFollowing(1, 1);
        verifyNoMoreInteractions(mAPIService);

        verify(mView, timeout(1000)).onLoadError(error);
        verify(mView, timeout(1000)).setRefreshing(false);
        verifyNoMoreInteractions(mView);
    }
}
