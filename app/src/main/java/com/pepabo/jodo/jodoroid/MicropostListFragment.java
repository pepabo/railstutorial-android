package com.pepabo.jodo.jodoroid;

import android.content.Intent;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

    @Override
    public void onStart() {
        super.onStart();

        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("EEE", "Long Clicked");

                Micropost m = (Micropost) parent.getItemAtPosition(position);
                long ownerId = m.getUser().getId();
                
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_delete_micropost)
                        .setMessage(R.string.description_delete_micropost)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("EEE", "DELETE !!!");
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                Log.e("EEE", m.getContent());
                return true;
            }
        });
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
