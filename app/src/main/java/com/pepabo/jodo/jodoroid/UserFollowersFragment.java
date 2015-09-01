package com.pepabo.jodo.jodoroid;

import android.os.Bundle;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class UserFollowersFragment extends UserListFragment {
    public static final int TYPE_FOLLOWERS = 0;
    public static final int TYPE_FOLLOWING = 1;


    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_TYPE = "type";


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

        long userId = getArguments().getLong(ARG_USER_ID);
        Observer<List<User>> observer = new Observer<List<User>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                showErrorToast(e);
                setUsers(new ArrayList<User>());
            }

            @Override
            public void onNext(List<User> users) {
                setUsers(users);
            }
        };

        switch (getArguments().getInt(ARG_TYPE)) {
            case TYPE_FOLLOWERS:
                getAPIService()
                        .fetchFollowers(userId, 1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);
                break;
            case TYPE_FOLLOWING:
                getAPIService()
                        .fetchFollowing(userId, 1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private APIService getAPIService() {
        return ((JodoroidApplication) getActivity().getApplication()).getAPIService();
    }

    private void showErrorToast(Throwable e) {
        String message = getString(R.string.toast_load_failure);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
