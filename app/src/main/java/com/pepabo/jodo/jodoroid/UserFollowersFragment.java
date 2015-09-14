package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

public class UserFollowersFragment extends UserListFragment
        implements RefreshableView<List<User>> {

    static final String ARG_USER_ID = "user_id";
    static final String ARG_TYPE = "type";

    int mType;
    long mUserId;

    APIService mAPIService;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mAPIService = ((JodoroidApplication) getActivity().getApplication()).getAPIService();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserId = getArguments().getLong(ARG_USER_ID);
        mType = getArguments().getInt(ARG_TYPE);

        mPresenter = new UserFollowersPresenter(mAPIService, mType, mUserId);
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
    public void onNextModel(List<User> users) {
        setUsers(users);
    }

    @Override
    public void onMoreModel(List<User> users) {
        addUsers(users);
    }

    @Override
    public void onLoadError(Throwable e) {
        Toast.makeText(getActivity(),
                getString(R.string.toast_load_failure),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onLoadNextPage() {
        mPresenter.onLoadNextPage();
    }

    @Override
    public ListFragment getFragment() {
        return this;
    }
}
