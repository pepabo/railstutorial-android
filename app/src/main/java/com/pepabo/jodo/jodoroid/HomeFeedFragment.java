package com.pepabo.jodo.jodoroid;

import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.Micropost;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class HomeFeedFragment extends MicropostListFragment {
    public static HomeFeedFragment newInstance() {
        HomeFeedFragment fragment = new HomeFeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFeedFragment() {
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadHomeFeed();
    }

    private void loadHomeFeed() {
        ((JodoroidApplication) getActivity().getApplication()).getAPIService()
                .fetchFeed(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Micropost>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(),
                                getString(R.string.toast_load_failure), Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onNext(List<Micropost> microposts) {
                        setMicroposts(microposts);
                    }
                });
    }
}
