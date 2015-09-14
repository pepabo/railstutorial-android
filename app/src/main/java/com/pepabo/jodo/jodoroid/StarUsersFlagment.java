package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

public class StarUsersFlagment extends UserListFragment implements RefreshableView<List<User>> {

    APIService mAPIService;
    RefreshPresenter<List<User>> mRefreshPresenter;

    public static StarUsersFlagment newInstance() {
        StarUsersFlagment fragment = new StarUsersFlagment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public StarUsersFlagment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mAPIService = ((JodoroidApplication) getActivity().getApplication()).getAPIService();
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        mRefreshPresenter = new StarUsersPresenter(mAPIService);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRefreshPresenter.setView(this);
        mRefreshPresenter.refresh();
    }

    @Override
    public void onDestroyView() {
        mRefreshPresenter.setView(null);
        super.onDestroyView();
    }

    @Override
    public void onNextModel(List<User> stars) {
        setUsers(stars);
    }

    @Override
    public void onMoreModel(List<User> stars) {
        addUsers(stars);
    }

    @Override
    public void onLoadError(Throwable e) {
        Toast.makeText(getActivity(), getText(R.string.toast_load_failure), Toast.LENGTH_SHORT).show();

    }

    @Override
    public ListFragment getFragment() {
        return this;
    }
}
