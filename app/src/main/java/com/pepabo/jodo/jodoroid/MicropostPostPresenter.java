package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class MicropostPostPresenter extends BasePresenter<MicropostPostView> {
    @Inject
    APIService mAPIService;

    @Inject
    ExpirationManager mExpirationManager;

    Subscription mSubscription = Subscriptions.unsubscribed();

    @Inject
    MicropostPostPresenter() {
    }

    public void submit() {
        final MicropostPostView view = getView();
        if(view != null && mSubscription.isUnsubscribed()) {
            mSubscription = mAPIService
                    .createMicropost(view.getContent(), view.getAttachment())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new UpdateSubscriber());
        }
    }

    class UpdateSubscriber extends Subscriber<Micropost> {
        @Override
        public void onStart() {
            final MicropostPostView view = getView();
            if(view != null) {
                view.showProgress(true);
            }
        }

        @Override
        public void onCompleted() {
            mExpirationManager.expire();

            final MicropostPostView view = getView();
            if(view != null) {
                view.finish();
            }
        }

        @Override
        public void onError(Throwable e) {
            final MicropostPostView view = getView();
            if(view != null) {
                view.showProgress(false);
                view.onError(e);
            }
        }

        @Override
        public void onNext(Micropost micropost) {
        }
    }
}
