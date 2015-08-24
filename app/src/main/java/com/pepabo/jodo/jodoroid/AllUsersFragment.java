package com.pepabo.jodo.jodoroid;

import android.os.Bundle;

import com.pepabo.jodo.jodoroid.dummy.DummyContent;

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

        setUsers(DummyContent.getAllUsers());
    }
}
