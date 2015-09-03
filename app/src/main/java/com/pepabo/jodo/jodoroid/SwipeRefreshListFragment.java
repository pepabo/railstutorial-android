package com.pepabo.jodo.jodoroid;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SwipeRefreshListFragment extends ListFragment
        implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext());

        final View view = super.onCreateView(inflater, mSwipeRefreshLayout, savedInstanceState);
        if (view != null) {
            mSwipeRefreshLayout.addView(
                    view,
                    new SwipeRefreshLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);

        return mSwipeRefreshLayout;
    }

    @Override
    public void onDestroyView() {
        mSwipeRefreshLayout = null;
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Workaround https://stackoverflow.com/questions/27411397/new-version-of-swiperefreshlayout-causes-wrong-draw-of-views
        if(mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.clearAnimation();
        }
    }

    @Override
    public void onRefresh() {
        // nop by default
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    class ListFragmentSwipeRefreshLayout extends SwipeRefreshLayout {
        public ListFragmentSwipeRefreshLayout(Context context) {
            super(context);
        }

        @Override
        public boolean canChildScrollUp() {
            return getListView().canScrollVertically(-1);
        }
    }
}
