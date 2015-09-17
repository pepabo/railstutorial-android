package com.pepabo.jodo.jodoroid;

import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;

public class UserFollowersFragment extends UserListFragment
        implements RefreshableView<List<User>> {

    static final String ARG_USER_ID = "user_id";
    static final String ARG_TYPE = "type";

    @dagger.Module
    static class Module {
        int mType;
        long mUserId;

        public Module(int type, long userId) {
            this.mType = type;
            this.mUserId = userId;
        }

        @PerFragment
        @Provides
        RefreshPresenter<List<User>> providePresenter(APIService apiService, ExpirationManager expirationManager) {
            return new UserFollowersPresenter(apiService, expirationManager, mType, mUserId);
        }
    }

    @PerFragment
    @dagger.Component(dependencies = ApplicationComponent.class, modules = Module.class)
    interface Component {
        void inject(UserFollowersFragment fragment);
    }

    @Inject
    RefreshPresenter<List<User>> mPresenter;

    public static UserFollowersFragment newInstance(long userId, int type) {
        UserFollowersFragment fragment = new UserFollowersFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public UserFollowersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final long userId = getArguments().getLong(ARG_USER_ID);
        final int type = getArguments().getInt(ARG_TYPE);

        DaggerUserFollowersFragment_Component.builder()
                .applicationComponent(((JodoroidApplication) getActivity().getApplication()).component())
                .module(new Module(type, userId))
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
    public void onNextModel(List<User> users) {
        setItems(users);
    }

    @Override
    public void onMoreModel(List<User> users) {
        addItems(users);
    }

    @Override
    public void onLoadError(Throwable e) {
        Toast.makeText(getActivity(),
                getString(R.string.toast_load_failure),
                Toast.LENGTH_SHORT).show();
        super.onLoadError(e);
    }

    @Override
    protected void onLoadNextPage() {
        mPresenter.loadNextPage();
    }
}
