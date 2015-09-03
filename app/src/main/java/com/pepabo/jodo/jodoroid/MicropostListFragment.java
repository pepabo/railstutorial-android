package com.pepabo.jodo.jodoroid;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MicropostListFragment extends SwipeRefreshListFragment {

    private List<Micropost> mMicroposts = new ArrayList<>();
    private ArrayAdapter<Micropost> mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MicropostListFragment() {
    }

    protected void setMicroposts(List<Micropost> microposts) {
        mMicroposts.clear();
        mMicroposts.addAll(microposts);

        if (mAdapter == null) {
            final Picasso picasso =
                    ((JodoroidApplication) getActivity().getApplication()).getPicasso();
            mAdapter = new MicropostsAdapter(getActivity(), picasso, mMicroposts);
            setListAdapter(mAdapter);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final Micropost micropost = (Micropost) l.getItemAtPosition(position);
        if (micropost != null) {
            final Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setAction(MainActivity.ACTION_VIEW_USER_PROFILE);
            intent.putExtra(MainActivity.EXTRA_USER_ID, micropost.getUser().getId());
            startActivity(intent);
        }
    }
}
