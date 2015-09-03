package com.pepabo.jodo.jodoroid;

import android.content.Context;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.lang.ref.WeakReference;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class ProfileEditPresenter {
    WeakReference<ProfileEditView> mView;
    APIService mAPIService;
    JodoAccount mAccount;

    Subscription mSubscription = Subscriptions.unsubscribed();

    FormItemValidator mEmailValidator;
    FormItemValidator mNameValidator;

    public ProfileEditPresenter(Context context, ProfileEditView view, APIService apiService, JodoAccount account) {
        mView = new WeakReference<>(view);
        mAPIService = apiService;
        mAccount = account;
        mEmailValidator = new EmailValidator(context);
        mNameValidator = new NameValidator(context);
    }

    void start() {
        if (mSubscription.isUnsubscribed()) {
            mSubscription = mAPIService.fetchMe(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new FetchSubscriber());
        }
    }

    void stop() {
        mSubscription.unsubscribe();
    }

    void submit() {
        if(mSubscription.isUnsubscribed()) {
            final ProfileEditView view = mView.get();
            if (view != null) {
                final String name = view.getName();
                final String email = view.getEmail();

                boolean error = false;

                mEmailValidator.validate(email);
                if (mEmailValidator.hasError()) {
                    view.setEmailError(mEmailValidator.getErrorMessage());
                    error = true;
                }

                mNameValidator.validate(name);
                if (mNameValidator.hasError()) {
                    view.setNameError(mNameValidator.getErrorMessage());
                    error = true;
                }

                if (!error) {
                    mSubscription = mAPIService.updateMe(name, email, null)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new UpdateSubscriber(email));
                }
            }
        }
    }

    class FetchSubscriber extends Subscriber<User> {
        @Override
        public void onStart() {
            final ProfileEditView view = mView.get();
            if (view != null) {
                view.showProgress(true);
            }
        }

        @Override
        public void onCompleted() {
            final ProfileEditView view = mView.get();
            if (view != null) {
                view.showProgress(false);
            }
        }

        @Override
        public void onError(Throwable e) {
            final ProfileEditView view = mView.get();
            if (view != null) {
                view.finish();
            }
        }

        @Override
        public void onNext(User user) {
            final ProfileEditView view = mView.get();
            if (view != null) {
                view.setEmail(mAccount.getEmail());
                view.setName(user.getName());
            }
        }
    }

    class UpdateSubscriber extends Subscriber<User> {
        final String mEmail;

        public UpdateSubscriber(String email) {
            mEmail = email;
        }

        @Override
        public void onStart() {
            final ProfileEditView view = mView.get();
            if (view != null) {
                view.showProgress(true);
            }
        }

        @Override
        public void onCompleted() {
            final ProfileEditView view = mView.get();
            if (view != null) {
                view.showProgress(false);
            }
        }

        @Override
        public void onError(Throwable e) {
            final ProfileEditView view = mView.get();
            if (view != null) {
                view.finish();
            }
        }

        @Override
        public void onNext(User unused) {
            final ProfileEditView view = mView.get();
            if (view != null) {
                mAccount.changeEmail(mEmail);
                view.finish();
            }
        }
    }
}
