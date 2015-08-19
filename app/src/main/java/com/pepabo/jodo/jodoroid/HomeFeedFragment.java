package com.pepabo.jodo.jodoroid;

import android.os.Bundle;

import com.pepabo.jodo.jodoroid.dummy.DummyContent;

public class HomeFeedFragment extends MicropostFragment {
    public static HomeFeedFragment newInstance() {
        HomeFeedFragment fragment = new HomeFeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMicroposts(DummyContent.getHomeTimeline());
    }
}
