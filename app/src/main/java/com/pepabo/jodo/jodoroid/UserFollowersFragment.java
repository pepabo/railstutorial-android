package com.pepabo.jodo.jodoroid;

import android.os.Bundle;

import com.pepabo.jodo.jodoroid.dummy.DummyContent;

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

        switch (getArguments().getInt(ARG_USER_ID)) {
            case TYPE_FOLLOWERS:
                setUsers(DummyContent.getFollowers(getArguments().getLong(ARG_USER_ID)));
                break;
            case TYPE_FOLLOWING:
                setUsers(DummyContent.getFollowing(getArguments().getLong(ARG_USER_ID)));
                break;
            default:
                assert false;
        }
    }
}
