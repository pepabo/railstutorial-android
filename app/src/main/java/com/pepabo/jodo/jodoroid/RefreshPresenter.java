package com.pepabo.jodo.jodoroid;

import android.view.View;

import com.pepabo.jodo.jodoroid.models.Micropost;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public abstract class RefreshPresenter<Model> {

    WeakReference<RefreshableView<Model>> mView;
    Subscription mSubscription = Subscriptions.unsubscribed();

    public RefreshPresenter(RefreshableView<Model> view) {
        this.mView = new WeakReference<>(view);
    }

    protected int mPage;
    protected boolean mStopped;

    protected abstract Observable<Model> getObservable();
    protected Observer<Model> getObserver(){
        return new RefreshSubscriber();
    }

    public void refresh() {
        if (mSubscription.isUnsubscribed()) {
            mSubscription = getObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        }
    }

    protected void resetPagination() {
        mPage    = 1;
        mStopped = false;
    }

    public void noMorePagination() {
        mStopped = true;
    }

    public RefreshableView<Model> getView() {
        return mView.get();
    }

    class RefreshSubscriber extends Subscriber<Model> {
        @Override
        public void onStart() {
            final RefreshableView<Model> view = mView.get();
            if (view != null) {
                view.setRefreshing(true);
            }
        }

        @Override
        public void onNext(Model model) {
            final RefreshableView<Model> view = mView.get();
            if (view != null) {
                view.onNextModel(model);
            }
        }

        @Override
        public void onCompleted() {
            final RefreshableView<Model> view = mView.get();
            if (view != null) {
                view.setRefreshing(false);
            }
        }

        @Override
        public void onError(Throwable e) {
            final RefreshableView<Model> view = mView.get();
            if (view != null) {
                view.onLoadError(e);
                view.setRefreshing(false);
            }
        }
    }
}
