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
public class UserListFragment extends SwipeRefreshListFragment<User> {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserListFragment() {
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

    @Override
    protected ArrayAdapter<User> createAdapter(List<User> list) {
        final Picasso picasso =
                ((JodoroidApplication) getActivity().getApplication()).getPicasso();
        return new UsersAdapter(getActivity(), picasso, list);
    }
}
