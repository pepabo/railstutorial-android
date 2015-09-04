package com.pepabo.jodo.jodoroid;

import android.content.Context;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
                            .flatMap(new Func1<User, Observable<User>>() {
                                @Override
                                public Observable<User> call(final User user) {
                                    return mAccount.changeEmail(email)
                                            .map(new Func1<JodoAccount, User>() {
                                                @Override
                                                public User call(JodoAccount unused) {
                                                    return user;
                                                }
                                            });
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new UpdateSubscriber());
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
                view.onError(e);
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
                view.onError(e);
                view.finish();
            }
        }

        @Override
        public void onNext(User unused) {
            final ProfileEditView view = mView.get();
            if (view != null) {
                view.finish();
            }
        }
    }
}
