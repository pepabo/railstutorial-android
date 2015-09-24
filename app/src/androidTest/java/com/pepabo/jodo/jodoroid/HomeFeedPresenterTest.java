package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
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
        // The first page of the feed contains a micropost
        final List<Micropost> feed1 = Arrays.asList(new Micropost());
        // and the second is empty, indicating the end of pagination
        final List<Micropost> feed2 = new ArrayList<>();

        when(mAPIService.fetchFeed(1)).thenReturn(
                Observable.just(feed1).subscribeOn(Schedulers.io())
        );
        when(mAPIService.fetchFeed(2)).thenReturn(
                Observable.just(feed2).subscribeOn(Schedulers.io())
        );

        // When the view is first shown,
        mPresenter.refresh();

        // page 1 is fetched and displayed.
        verify(mAPIService, timeout(1000)).fetchFeed(1);
        verify(mView, timeout(1000)).onNextModel(feed1);
        verify(mView, timeout(1000)).setRefreshing(false);

        // Then, by scrolling to the bottom of view,
        mPresenter.loadNextPage();

        // the next page is fetched and
        verify(mAPIService, timeout(1000)).fetchFeed(2);

        // delivered to the view.
        verify(mView, timeout(1000)).onMoreModel(feed2);
        verify(mView, timeout(1000).times(2)).setRefreshing(false);

        // Scrolling to the end again does nothing.
        mPresenter.loadNextPage();
        verifyNoMoreInteractions(mView);
        verifyNoMoreInteractions(mAPIService);

        reset(mView);
        reset(mAPIService);

        when(mAPIService.fetchFeed(1)).thenReturn(
                Observable.just(feed1).subscribeOn(Schedulers.io())
        );

        // When user refreshes the view,
        mPresenter.refresh();

        // the first page is again fetched
        verify(mAPIService, timeout(1000)).fetchFeed(1);
        verify(mView, timeout(1000)).onNextModel(feed1);
        verify(mView, timeout(1000)).setRefreshing(false);

        verifyNoMoreInteractions(mView);
        verifyNoMoreInteractions(mAPIService);
    }

    public void testRefreshError() {
        final Throwable e = new Error();

        when(mAPIService.fetchFeed(1)).thenReturn(
                Observable.<List<Micropost>>error(e).subscribeOn(Schedulers.io())
        );

        mPresenter.refresh();

        verify(mAPIService, timeout(1000)).fetchFeed(1);
        verify(mView, timeout(1000)).onLoadError(e);
        verify(mView, timeout(1000)).setRefreshing(false);
    }
}
