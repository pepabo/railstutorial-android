package com.pepabo.jodo.jodoroid;

import android.content.Context;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class PasswordChangePresenter {
    PasswordChangeView mView;

    PasswordValidator mPasswordValidator;

    APIService mAPIService;
    Subscription mSubscription = Subscriptions.unsubscribed();


    public PasswordChangePresenter(Context context, APIService apiService) {
        this.mPasswordValidator = new PasswordValidator(context);

        this.mAPIService = apiService;
    }

    public PasswordChangeView getView() {
        return mView;
    }

    public void setView(PasswordChangeView view) {
        this.mView = view;
    }

    public void submit() {
        final PasswordChangeView view = mView;

        if (view != null && mSubscription.isUnsubscribed()) {
            final String password = view.getPassword();

            boolean cancel = false;

            mPasswordValidator.validate(password);
            if (mPasswordValidator.hasError()) {
                view.setPasswordError(mPasswordValidator.getErrorMessage());
                cancel = true;
            }

            if (!cancel) {
                mSubscription = mAPIService.updateMe(null, null, password)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new UpdateSubscriber());
            }
        }
    }

    class UpdateSubscriber extends Subscriber<User> {
        @Override
        public void onStart() {
            final PasswordChangeView view = getView();
            if (view != null) {
                view.showProgress(true);
            }
        }

        @Override
        public void onCompleted() {
            final PasswordChangeView view = getView();
            if (view != null) {
                view.showProgress(false);
            }
        }

        @Override
        public void onError(Throwable e) {
            final PasswordChangeView view = getView();
            if (view != null) {
                view.showProgress(false);
                view.onError(e);
            }
        }

        @Override
        public void onNext(User user) {
            final PasswordChangeView view = getView();
            if (view != null) {
                view.finish();
            }
        }
    }
}
