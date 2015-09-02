package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class HomeFeedFragment extends MicropostListFragment {

    Subscription mSubscription = Subscriptions.unsubscribed();
    APIService mAPIService;

    public static HomeFeedFragment newInstance() {
        HomeFeedFragment fragment = new HomeFeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFeedFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mAPIService = ((JodoroidApplication) activity.getApplication()).getAPIService();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadHomeFeed();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        loadHomeFeed();
    }

    void loadHomeFeed() {
        if (mSubscription.isUnsubscribed()) {
            mSubscription = mAPIService.fetchFeed(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new FeedSubscriber(this));
        }
    }

    static class FeedSubscriber extends Subscriber<List<Micropost>> {
        WeakReference<HomeFeedFragment> mFragment;

        public FeedSubscriber(HomeFeedFragment fragment) {
            this.mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void onStart() {
            final HomeFeedFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.setRefreshing(true);
            }
        }

        @Override
        public void onNext(List<Micropost> microposts) {
            final HomeFeedFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.setMicroposts(microposts);
            }
        }

        @Override
        public void onCompleted() {
            final HomeFeedFragment fragment = mFragment.get();
            if (fragment != null) {
                fragment.setRefreshing(false);
            }
        }

        @Override
        public void onError(Throwable e) {
            final HomeFeedFragment fragment = mFragment.get();
            if (fragment != null) {
                if (fragment.isAdded()) {
                    Toast.makeText(fragment.getActivity(),
                            fragment.getString(R.string.toast_load_failure), Toast.LENGTH_SHORT)
                            .show();
                }
                fragment.setRefreshing(false);
            }
        }
    }
}
