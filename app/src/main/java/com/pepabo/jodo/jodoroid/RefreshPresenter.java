package com.pepabo.jodo.jodoroid;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public abstract class RefreshPresenter<Model> {

    RefreshableView<Model> mView;
    Subscription mSubscription = Subscriptions.unsubscribed();

    int mPage;
    boolean mStopped;

    public RefreshPresenter() {
        resetPagination();
    }

    public void setView(RefreshableView<Model> view) {
        mView = view;
    }

    public RefreshableView<Model> getView() {
        return mView;
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

    public void noMorePagination() {
        mStopped = true;
    }


    class RefreshSubscriber extends Subscriber<Model> {
        final int mPage;

        public RefreshSubscriber(int page) {
            this.mPage = page;
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
                if (mPage == 1) {
                    view.onNextModel(model);
                } else {
                    view.onMoreModel(model);
                }
            }
        }

        @Override
        public void onCompleted() {
            RefreshPresenter.this.mPage++;

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
