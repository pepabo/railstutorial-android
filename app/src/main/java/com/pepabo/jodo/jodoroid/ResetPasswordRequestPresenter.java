package com.pepabo.jodo.jodoroid;

import android.content.Context;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;

import java.sql.ResultSet;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class ResetPasswordRequestPresenter {
    private int RESULT_OK = 1;

    ResetPasswordRequestView mView;

    EmailValidator mEmailValidator;

    APIService mAPIService;
    Subscription mSubscription = Subscriptions.unsubscribed();

    public ResetPasswordRequestPresenter(Context context, APIService apiService) {
        this.mEmailValidator = new EmailValidator(context);

        this.mAPIService = apiService;
    }

    public ResetPasswordRequestView getView() {
        return mView;
    }

    public void submit() {
        final ResetPasswordRequestView view = mView;

        if (view != null && mSubscription.isUnsubscribed()) {
            final String email = view.getEmail();

            boolean cancel = false;

            mEmailValidator.validate(email);
            if (mEmailValidator.hasError()) {
                view.setEmailError(mEmailValidator.getErrorMessage());
                cancel = true;
            }

            if (!cancel) {
                mSubscription = mAPIService.requestPasswordReset(email)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ResetPasswordRequestSubscriber());
            }
        }
    }

    class ResetPasswordRequestSubscriber extends Subscriber<Void> {
        @Override
        public void onStart() {
            final ResetPasswordRequestView view = getView();
            if(view != null) {
                view.showProgress(true);
            }
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            final ResetPasswordRequestView view = getView();
            final ResetPasswordRequest activity = new ResetPasswordRequest();
            if(view != null) {
                view.showProgress(false);
                Toast.makeText(activity, ErrorUtils.getMessage(e), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNext(Void aVoid) {
            final ResetPasswordRequestView view = getView();
            final ResetPasswordRequest activity = new ResetPasswordRequest();
            if(view != null) {
                view.setResult(RESULT_OK);
                Toast.makeText(activity, R.string.toast_check_email_reset_password, Toast.LENGTH_LONG).show();
                view.finish();
            }
        }

    }
}
