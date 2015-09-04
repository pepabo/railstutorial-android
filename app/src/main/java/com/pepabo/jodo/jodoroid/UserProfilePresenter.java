package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class UserProfilePresenter extends RefreshPresenter<User> {
    APIService mAPIService;
    long mUserId;

    public UserProfilePresenter(RefreshableView<User> view, APIService apiService, long userId) {
        super(view);
        mAPIService = apiService;
        mUserId = userId;
    }

    @Override
    public void refresh() {
        super.refresh();
        resetPagination();
    }

    @Override
    protected Observable<User> getObservable() {
        return mAPIService.fetchUser(mUserId, 1);
    }

    public Observable<User> loadNextPage() {
        mPage++;
        return mAPIService.fetchUser(mUserId, mPage);
    }

    public void onLoadNextPage() {

        if (!mSubscription.isUnsubscribed() || mStopped) {
            return;
        }

        mSubscription = loadNextPage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onLoadError(e);
                    }

                    @Override
                    public void onNext(User user) {
                        if (user.getMicroposts().size() == 0) noMorePagination();
                        getView().onMoreModel(user);
                    }
                });
    }
}
