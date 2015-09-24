package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.*;

public class AccountInfoPresenterTest extends AndroidTestCase {
    APIService mAPIService;
    AccountInfoPresenter mPresenter;
    AccountInfoView mView;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mAPIService = mock(APIService.class);
        mView = mock(AccountInfoView.class);
        mPresenter = new AccountInfoPresenter(mAPIService);
        mPresenter.setView(mView);
    }

    public void testRefreshSuccess() {
        final User user = new User();

        when(mAPIService.fetchMe(1)).thenReturn(
                Observable.just(user).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchMe(1);
        verifyNoMoreInteractions(mAPIService);

        verify(mView, timeout(1000)).setAccountUser(user);
        verifyNoMoreInteractions(mView);
    }

    public void testRefreshFailure() {
        final Throwable error = new Error();

        when(mAPIService.fetchMe(1)).thenReturn(
                Observable.<User>error(error).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchMe(1);
        verifyNoMoreInteractions(mAPIService);

        verifyZeroInteractions(mView);
    }
}
