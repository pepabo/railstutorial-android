package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.*;

public class UserProfilePresenterTest extends AndroidTestCase {
    APIService mAPIService;
    UserProfilePresenter mPresenter;
    ExpirationManager mExpirationManager;
    RefreshableView<User> mView;

    final private long USER_ID = 1;
    final private int  PAGE    = 1;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mAPIService = mock(APIService.class);
        mView = mock(RefreshableView.class);
        mExpirationManager = new ExpirationManager();
        mPresenter = new UserProfilePresenter(mAPIService, mExpirationManager, USER_ID);
        mPresenter.setView(mView);
    }

    public void testRefreshSuccess() {
        final User user = new User();

        when(mAPIService.fetchUser(USER_ID, PAGE)).thenReturn(
                Observable.just(user).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchUser(USER_ID, PAGE);
        verifyNoMoreInteractions(mAPIService);

        verify(mView, timeout(1000)).onNextModel(user);
        verify(mView, timeout(1000)).setRefreshing(false);
        verifyNoMoreInteractions(mView);
    }

    public void testRefreshFailure() {
        final Throwable error = new Error();

        when(mAPIService.fetchUser(USER_ID, PAGE)).thenReturn(
                Observable.<User>error(error).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchUser(USER_ID, PAGE);
        verifyNoMoreInteractions(mAPIService);

        verify(mView, timeout(1000)).onLoadError(error);
        verify(mView, timeout(1000)).setRefreshing(false);
        verifyNoMoreInteractions(mView);
    }
}
