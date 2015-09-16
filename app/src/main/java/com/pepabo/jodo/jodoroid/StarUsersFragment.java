package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

public class StarUsersFragment extends UserListFragment implements RefreshableView<List<User>> {

    APIService mAPIService;
    RefreshPresenter<List<User>> mRefreshPresenter;

    public static StarUsersFragment newInstance() {
        StarUsersFragment fragment = new StarUsersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public StarUsersFragment() {
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
    public void onRefresh() {
        super.onRefresh();

        mRefreshPresenter.refresh();
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
