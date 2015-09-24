package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.*;

public class PasswordChangePresenterTest extends AndroidTestCase {
    final String NEW_PASSWORD = "password123";

    APIService mAPIService;
    PasswordChangeView mView;
    PasswordChangePresenter mPresenter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mAPIService = mock(APIService.class);
        mView = mock(PasswordChangeView.class);
        mPresenter = new PasswordChangePresenter(getContext(), mAPIService);
        mPresenter.setView(mView);
    }

    public void testSubmitSuccess() {
        final User user = new User();

        when(mAPIService.updateMe(null, null, NEW_PASSWORD)).thenReturn(
                Observable.just(user).subscribeOn(Schedulers.io())
        );
        when(mView.getPassword()).thenReturn(NEW_PASSWORD);

        mPresenter.submit();

        verify(mAPIService, timeout(1000)).updateMe(null, null, NEW_PASSWORD);
        verifyNoMoreInteractions(mAPIService);

        verify(mView, timeout(1000)).showProgress(true);
        verify(mView, timeout(1000)).finish();
    }

    public void testSubmitFailure() {
        final Throwable error = new Error();

        when(mAPIService.updateMe(null, null, NEW_PASSWORD)).thenReturn(
                Observable.<User>error(error)
        );
        when(mView.getPassword()).thenReturn(NEW_PASSWORD);

        mPresenter.submit();

        verify(mAPIService, timeout(1000)).updateMe(null, null, NEW_PASSWORD);
        verifyNoMoreInteractions(mAPIService);

        verify(mView, timeout(1000)).showProgress(false);
        verify(mView, timeout(1000)).onError(error);
    }

    public void testSubmitInvalidPassword() {
        String shortPassword = "123";

        when(mView.getPassword()).thenReturn(shortPassword);

        mPresenter.submit();

        verify(mAPIService, never()).updateMe(null, null, shortPassword);
        verify(mView, timeout(1000)).setPasswordError(anyString());
    }
}
