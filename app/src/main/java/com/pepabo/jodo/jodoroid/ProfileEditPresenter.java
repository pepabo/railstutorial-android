package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import rx.Subscriber;

public class ProfileEditPresenter {
    ProfileEditView mView;
    APIService mAPIService;

    public ProfileEditPresenter(ProfileEditView view, APIService apiService) {
        mView = view;
        mAPIService = apiService;
    }

    void start() {

    }

    class FetchSubscriber extends Subscriber<User> {
        @Override
        public void onStart() {
            mView.showProgress(true);
        }

        @Override
        public void onCompleted() {
            mView.showProgress(false);
        }

        @Override
        public void onError(Throwable e) {
            mView.finish();
        }

        @Override
        public void onNext(User user) {

        }
    }

    class UpdateSubscriber extends Subscriber<Void> {
        @Override
        public void onStart() {
            mView.showProgress(true);
        }

        @Override
        public void onCompleted() {
            mView.showProgress(false);
        }

        @Override
        public void onError(Throwable e) {
            mView.finish();
        }

        @Override
        public void onNext(Void unused) {

        }
    }
}
