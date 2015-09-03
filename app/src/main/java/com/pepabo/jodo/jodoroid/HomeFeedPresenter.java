package com.pepabo.jodo.jodoroid;

import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class HomeFeedPresenter extends RefreshPresenter<List<Micropost>> {
    APIService mAPIService;
    int mPage;
    boolean mStopped;

    public HomeFeedPresenter(RefreshableView<List<Micropost>> view, APIService apiService) {
        super(view);
        mAPIService = apiService;
        reset();
    }

    @Override
    public void refresh() {
        super.refresh();

        reset();
    }

    @Override
    protected Observable<List<Micropost>> getObservable() {
        return mAPIService.fetchFeed(1);
    }

    public Observable<List<Micropost>> loadNextPage() {
        mPage++;
        return mAPIService.fetchFeed(mPage);
    }

    public void stop() {
        mStopped = true;
    }

    public void onLoadNextPage() {

        if (!mSubscription.isUnsubscribed() || mStopped) {
            return;
        }

        mSubscription = loadNextPage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Micropost>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onLoadError(e);
                    }

                    @Override
                    public void onNext(List<Micropost> microposts) {
                        if (microposts.size() == 0) stop();
                        getView().onMoreModel(microposts);
                    }
                });
    }

    private void reset() {
        mStopped = false;
        mPage    = 1;
    }
}
