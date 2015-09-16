package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

public class AllUsersFragment extends UserListFragment
        implements RefreshableView<List<User>> {

    APIService mAPIService;
    RefreshPresenter<List<User>> mPresenter;

    public static AllUsersFragment newInstance() {
        AllUsersFragment fragment = new AllUsersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AllUsersFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mAPIService = ((JodoroidApplication) getActivity().getApplication()).getAPIService();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new AllUsersPresenter(mAPIService);
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
}
