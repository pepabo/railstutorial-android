package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.content.Context;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Session;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class ResetPasswordPresenter {
    ResetPasswordView mView;

    PasswordValidator mPasswordValidator;

    APIService mAPIService;
    Subscription mSubscription = Subscriptions.unsubscribed();

    public ResetPasswordPresenter(Context context, APIService apiService) {
        this.mPasswordValidator = new PasswordValidator(context);

        mAPIService = apiService;
    }

    public ResetPasswordView getView() {
        return mView;
    }

    public void setView(ResetPasswordView view) {
        mView = view;
    }

    public void submit() {
        final ResetPasswordView view = mView;

        if(view != null && mSubscription.isUnsubscribed()) {
            final String token = view.getToken();
            final String email = view.getEmail();
            final String password = view.getPassword();

            boolean cancel = false;

            mPasswordValidator.validate(password);
            if(mPasswordValidator.hasError()) {
                view.setPasswordError(mPasswordValidator.getErrorMessage());
                cancel = true;
            }

            if(!cancel) {
                mSubscription = mAPIService.resetPassword(token, email, password)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ResetPasswordSubscriber());
            }
        }
    }

    class ResetPasswordSubscriber extends Subscriber<Session> {
        @Override
        public void onStart() {
            final ResetPasswordView view = getView();
            if(view != null) {
                view.showProgress(true);
            }
        }

        @Override
        public void onCompleted() {
            final ResetPasswordView view = getView();
            if(view != null) {
                view.getJodoroidApplication().doIntentTransition(WelcomeActivity.class);
                view.finish();
            }
        }

        @Override
        public void onError(Throwable e) {
            final ResetPasswordView view = getView();
            if(view != null) {
                view.showProgress(false);
                view.onError(e);
            }
        }

        @Override
        public void onNext(Session session) {
            final ResetPasswordView view = getView();
            final Activity activity = view.getActivity();
            final String email = view.getEmail();
            if (view != null) {
                JodoAccount.addAccount(activity, email, session);
                view.onSuccess();
            }
        }
    }
}
