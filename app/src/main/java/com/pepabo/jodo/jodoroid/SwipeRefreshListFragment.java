package com.pepabo.jodo.jodoroid;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import android.widget.FrameLayout;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.OnClick;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

abstract public class SwipeRefreshListFragment<Model> extends ListFragment
        implements SwipeRefreshLayout.OnRefreshListener {

    FrameLayout mLayout;
    View mReloadView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayAdapter<Model> mAdapter;

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        ListView v = getListView();
        v.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((visibleItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount)) {
                    onLoadNextPage();
                }
            }
        });
    }

    protected void onLoadNextPage() {
        // nop. will be overridden.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = new FrameLayout(container.getContext());
        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext());
        mSwipeRefreshLayout.setColorSchemeResources(R.color.material_deep_teal_500);

        final View view = super.onCreateView(inflater, mSwipeRefreshLayout, savedInstanceState);
        mReloadView = inflater.inflate(R.layout.view_reload, mLayout, false);

        if (view != null) {
            mSwipeRefreshLayout.addView(
                    view,
                    new SwipeRefreshLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }

        mLayout.addView(
                mSwipeRefreshLayout,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        mLayout.addView(
                mReloadView,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        mSwipeRefreshLayout.setOnRefreshListener(this);
        ButterKnife.bind(new ViewHolder(), mLayout);

        return mLayout;
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
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.clearAnimation();
        }
    }

    @Override
    public void onRefresh() {
        // nop by default
    }

    public class ViewHolder {
        @OnClick(R.id.reload_button)
        public void refresh() {
            onRefresh();
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mReloadView.setVisibility(View.GONE);
        }
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

    public void onLoadError(Throwable e) {
        if(getListAdapter() == null) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mReloadView.setVisibility(View.VISIBLE);
        }
    }

    abstract protected ArrayAdapter<Model> createAdapter(List<Model> list);

    protected void setItems(List<Model> items) {
        if (mAdapter == null) {
            mAdapter = createAdapter(new ArrayList<Model>());
            setListAdapter(mAdapter);
        } else {
            mAdapter.clear();
        }

        mAdapter.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    protected void addItems(List<Model> items) {
        mAdapter.addAll(items);
        mAdapter.notifyDataSetChanged();
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
