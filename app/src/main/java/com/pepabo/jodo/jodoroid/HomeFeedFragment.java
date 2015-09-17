package com.pepabo.jodo.jodoroid;

import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;

public class HomeFeedFragment extends MicropostListFragment
        implements RefreshableView<List<Micropost>> {

    @dagger.Module
    static class Module {
        public Module() {
        }

        @PerFragment
        @Provides
        RefreshPresenter<List<Micropost>> providePresenter(APIService apiService, ExpirationManager expirationManager) {
            return new HomeFeedPresenter(apiService, expirationManager);
        }
    }

    @PerFragment
    @dagger.Component(dependencies = ApplicationComponent.class, modules = Module.class)
    interface Component {
        void inject(HomeFeedFragment fragment);
    }

    @Inject
    RefreshPresenter<List<Micropost>> mPresenter;

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

        DaggerHomeFeedFragment_Component.builder()
                .applicationComponent(((JodoroidApplication) getActivity().getApplication()).component())
                .module(new Module())
                .build().inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter.setView(this);
        mPresenter.refresh();
    }

    @Override
    public void onDestroyView() {
        mPresenter.setView(null);
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.checkUpdate();
    }

    @Override
    public void onNextModel(List<Micropost> microposts) {
        setItems(microposts);
    }

    @Override
    public void onLoadError(Throwable e) {
        Toast.makeText(getActivity(),
                getString(R.string.toast_load_failure),
                Toast.LENGTH_SHORT).show();
        super.onLoadError(e);
    }

    @Override
    public void onMoreModel(List<Micropost> microposts) {
        addItems(microposts);
    }

    @Override
    protected void onLoadNextPage() {
        mPresenter.loadNextPage();
    }
}
