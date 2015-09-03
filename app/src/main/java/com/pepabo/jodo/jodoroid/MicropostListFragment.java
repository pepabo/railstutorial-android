package com.pepabo.jodo.jodoroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.Micropost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

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
    public MicropostListFragment() { }


    @Override
    public void onStart() {
        super.onStart();

        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Micropost m = (Micropost) parent.getItemAtPosition(position);
                long ownerId = m.getUser().getId();

                if (JodoAccounts.isMe(getActivity().getApplicationContext(), ownerId)) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.title_delete_micropost)
                            .setMessage(R.string.description_delete_micropost)
                            .setPositiveButton(R.string.ok, new PositiveButtonClickListener(m))
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                    return true;
                } else {
                    return false;
                }
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

    protected void addMicroposts(List<Micropost> microposts) {
        mMicroposts.addAll(microposts);
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

    private class PositiveButtonClickListener implements DialogInterface.OnClickListener {
        private Micropost mMicropost;

        PositiveButtonClickListener(Micropost m) {
            mMicropost = m;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            ((JodoroidApplication) getActivity().getApplication()).getAPIService()
                    .deleteMicropost(mMicropost.getId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Void>() {

                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(
                                    getActivity(),
                                    R.string.toast_deletion_failed,
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        @Override
                        public void onNext(Void v) {
                            Toast.makeText(
                                    getActivity(),
                                    R.string.toast_deletion_succeed,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        }
    }
}
