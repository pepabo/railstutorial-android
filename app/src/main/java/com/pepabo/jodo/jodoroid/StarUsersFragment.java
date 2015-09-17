package com.pepabo.jodo.jodoroid;

import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;

public class StarUsersFragment extends UserListFragment implements RefreshableView<List<User>> {

    @dagger.Module
    static class Module {
        public Module() {
        }

        @PerFragment
        @Provides
        RefreshPresenter<List<User>> providePresenter(APIService apiService, ExpirationManager expirationManager) {
            return new StarUsersPresenter(apiService, expirationManager);
        }
    }

    @PerFragment
    @dagger.Component(dependencies = ApplicationComponent.class, modules = Module.class)
    interface Component {
        void inject(StarUsersFragment fragment);
    }

    @Inject
    RefreshPresenter<List<User>> mPresenter;

    public static StarUsersFragment newInstance() {
        StarUsersFragment fragment = new StarUsersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public StarUsersFragment() {
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        DaggerStarUsersFragment_Component.builder()
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
    public void onNextModel(List<User> stars) {
        setItems(stars);
    }

    @Override
    public void onMoreModel(List<User> stars) {
        addItems(stars);
    }

    @Override
    public void onLoadError(Throwable e) {
        Toast.makeText(getActivity(), getText(R.string.toast_load_failure), Toast.LENGTH_SHORT).show();
        super.onLoadError(e);
    }
}
