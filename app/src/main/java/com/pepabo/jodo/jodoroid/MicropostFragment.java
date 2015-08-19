package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.dummy.DummyContent;
import com.pepabo.jodo.jodoroid.models.Micropost;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class MicropostFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;

    public static MicropostFragment newInstance() {
        MicropostFragment fragment = new MicropostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MicropostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new MicropostsAdapter(getActivity(), DummyContent.HOME_TIMELINE));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.HOME_TIMELINE.get(position));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Micropost id);
    }

    class MicropostsAdapter extends ArrayAdapter<Micropost> {
        LayoutInflater mInflater;
        List<Micropost> mObjects;

        public MicropostsAdapter(Context context, List<Micropost> objects) {
            super(context, 0, objects);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mObjects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = convertView != null ? convertView :
                    mInflater.inflate(R.layout.view_micropost, parent, false);

            final Micropost micropost = mObjects.get(position);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    fetchAvatar(micropost.getUser().getAvatar(), (ImageView) view.findViewById(R.id.avatar));
                }
            }).start();

            ((TextView)view.findViewById(R.id.username)).setText(micropost.getUser().getName());
            ((TextView)view.findViewById(R.id.content)).setText(micropost.getContent());
            ((TextView)view.findViewById(R.id.timestamp)).setText(formatDate(micropost.getCreatedAt()));

            return view;
        }

        String formatDate(Date date) {
            DateFormat f = DateFormat.getDateTimeInstance();
            return f.format(date);
        }

        void fetchAvatar(URI avatarUri, final ImageView view) {
            try {
                InputStream stream = (InputStream)avatarUri.toURL().getContent();
                try {
                    Log.i(MicropostsAdapter.class.getName(), String.format("Downloading %s", avatarUri));
                    final Drawable avatar = Drawable.createFromStream(stream, avatarUri.toString());
                    MicropostFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setImageDrawable(avatar);
                        }
                    });
                } finally {
                    stream.close();
                }
            } catch(IOException ex) {
                // set default image
            }
        }
    }
}