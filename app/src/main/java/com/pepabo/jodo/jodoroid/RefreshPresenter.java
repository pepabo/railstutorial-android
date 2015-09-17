package com.pepabo.jodo.jodoroid;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public abstract class RefreshPresenter<Model> extends BasePresenter<RefreshableView<Model>> {

    Subscription mSubscription = Subscriptions.unsubscribed();

    int mPage;
    boolean mStopped;
    long mUpdated;

    ExpirationManager mExpirationManager;

    public RefreshPresenter(ExpirationManager expirationManager) {
        mExpirationManager = expirationManager;
        resetPagination();
    }

    protected abstract Observable<Model> getObservable(int page);

    protected Observer<Model> getObserver(int page) {
        return new RefreshSubscriber(page);
    }

    public void refresh() {
        if (!mSubscription.isUnsubscribed()) return;

        resetPagination();
        mSubscription = getObservable(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(mPage));
    }

    public void checkUpdate() {
        if (mExpirationManager.isExpired(mUpdated)) {
            refresh();
        }
    }

    public void loadNextPage() {
        if (!mSubscription.isUnsubscribed() || mStopped) return;

        mSubscription = getObservable(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(mPage));
    }

    void resetPagination() {
        mPage = 1;
        mStopped = false;
    }

    abstract public boolean isLast(Model model);

    class RefreshSubscriber extends Subscriber<Model> {
        final int mLoadedPage;

        public RefreshSubscriber(int page) {
            this.mLoadedPage = page;
        }

        @Override
        public void onStart() {
            final RefreshableView<Model> view = getView();
            if (view != null) {
                view.setRefreshing(true);
            }
        }

        @Override
        public void onNext(Model model) {
            final RefreshableView<Model> view = getView();
            if (view != null) {
                if (mLoadedPage == 1) {
                    mUpdated = mExpirationManager.getCurrentTimestamp();
                    view.onNextModel(model);
                } else {
                    mStopped = isLast(model);
                    view.onMoreModel(model);
                }
            }
        }

        @Override
        public void onCompleted() {
            mPage++;

            final RefreshableView<Model> view = getView();
            if (view != null) {
                view.setRefreshing(false);
            }
        }

        @Override
        public void onError(Throwable e) {
            final RefreshableView<Model> view = getView();
            if (view != null) {
                view.onLoadError(e);
                view.setRefreshing(false);
            }
        }
    }
}
