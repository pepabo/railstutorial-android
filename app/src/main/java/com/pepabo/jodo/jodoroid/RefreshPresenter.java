package com.pepabo.jodo.jodoroid;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public abstract class RefreshPresenter<Model> {

    RefreshableView<Model> mView;
    Subscription mSubscription = Subscriptions.unsubscribed();

    protected int mPage;
    protected boolean mStopped;

    public RefreshPresenter() {
        resetPagination();
    }

    public void setView (RefreshableView<Model> view) {
        mView = view;
    }

    public RefreshableView<Model> getView() {
        return mView;
    }

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
        resetPagination();
    }

    protected void resetPagination() {
        mPage    = 1;
        mStopped = false;
    }

    public void noMorePagination() {
        mStopped = true;
    }


    class RefreshSubscriber extends Subscriber<Model> {
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
                view.onNextModel(model);
            }
        }

        @Override
        public void onCompleted() {
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

    abstract protected Observable<Model> loadNextPage(int pageNumber);

    public void onLoadNextPage() {
        if (!mSubscription.isUnsubscribed() || mStopped) return;

        mPage++;
        mSubscription = loadNextPage(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Model>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        final RefreshableView<Model> view = getView();
                        if (view != null) {
                            view.onLoadError(e);
                        }
                    }

                    @Override
                    public void onNext(Model model) {
                        final RefreshableView<Model> view = getView();
                        if (view != null) {
                            view.onMoreModel(model);
                        }
                    }
                });
    }
}
