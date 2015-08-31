package com.pepabo.jodo.jodoroid;

import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class AllUsersFragment extends UserListFragment {

    public static AllUsersFragment newInstance() {
        AllUsersFragment fragment = new AllUsersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AllUsersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadAllUsers();
    }

    private void loadAllUsers() {
        ((JodoroidApplication) getActivity().getApplication()).getAPIService()
                .fetchAllUsers(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(),
                                getString(R.string.toast_load_failure), Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onNext(List<User> users) {
                        setUsers(users);
                    }
                });
    }
}
