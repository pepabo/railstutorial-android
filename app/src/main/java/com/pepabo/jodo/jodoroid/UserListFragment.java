package com.pepabo.jodo.jodoroid;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pepabo.jodo.jodoroid.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class UserListFragment extends SwipeRefreshListFragment {

    private List<User> mUsers = new ArrayList<User>();
    private ArrayAdapter<User> mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserListFragment() {
    }

    protected void setUsers(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);

        if(mAdapter == null) {
            final Picasso picasso =
                    ((JodoroidApplication) getActivity().getApplication()).getPicasso();
            mAdapter = new UsersAdapter(getActivity(), picasso, mUsers);
            setListAdapter(mAdapter);
        }

        mAdapter.notifyDataSetChanged();
    }

    protected void addUsers(List<User> users) {
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final User user = (User) l.getItemAtPosition(position);
        if (user != null) {
            final Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setAction(MainActivity.ACTION_VIEW_USER_PROFILE);
            intent.putExtra(MainActivity.EXTRA_USER_ID, user.getId());
            startActivity(intent);
        }
    }
}
