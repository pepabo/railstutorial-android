package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.dummy.DummyContent;
import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends MicropostFragment {
    private static final String ARG_USER_ID = "user_id";

    private User mUser;

    public static UserProfileFragment newInstance(long userId) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = DummyContent.getUser(getArguments().getLong(ARG_USER_ID));
            setMicroposts(DummyContent.getUserTimeline(mUser));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ListView list = (ListView) view.findViewById(android.R.id.list);
        View profileView = inflater.inflate(R.layout.view_user_profile, list, false);
        list.addHeaderView(profileView);
        ((TextView) profileView.findViewById(R.id.textView_user_name)).setText(mUser.getName());
        new FetchAvatarTask((ImageView) profileView.findViewById(R.id.imageView_user_avatar))
                .execute(mUser.getAvatar());
        return view;
    }
}
