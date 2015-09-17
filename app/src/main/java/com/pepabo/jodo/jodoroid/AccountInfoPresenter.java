package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class AccountInfoPresenter extends BasePresenter<AccountInfoView> {

    Subscription mSubscription = Subscriptions.unsubscribed();

    APIService mAPIService;

    public AccountInfoPresenter(APIService apiService) {
        this.mAPIService = apiService;
    }

    void refresh() {
        if (mSubscription.isUnsubscribed()) {
            mSubscription = mAPIService.fetchMe(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RefreshSubscriber());
        }
    }

    class RefreshSubscriber extends Subscriber<User> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(User user) {
            final AccountInfoView view = getView();
            if (view != null) {
                view.setAccountUser(user);
            }
        }
    }
}
