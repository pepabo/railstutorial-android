package com.pepabo.jodo.jodoroid;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.dummy.DummyContent;
import com.pepabo.jodo.jodoroid.models.User;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends MicropostListFragment implements View.OnClickListener {
    private static final String ARG_USER_ID = "user_id";

    private User mUser;

    private View mProfileView;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Picasso picasso = ((JodoroidApplication) getActivity().getApplication()).getPicasso();

        ((TextView) mProfileView.findViewById(R.id.textView_user_name)).setText(mUser.getName());
        ((TextView) mProfileView.findViewById(R.id.textView_followers))
                .setText(Long.toString(mUser.getFollowersCount()));
        ((TextView) mProfileView.findViewById(R.id.textView_following))
                .setText(Long.toString(mUser.getFollowingCount()));
        picasso.load(mUser.getAvatarUrl()).fit().into((ImageView) mProfileView.findViewById(R.id.imageView_user_avatar));

        ((View) mProfileView.findViewById(R.id.layout_followers)).setOnClickListener(this);
        ((View) mProfileView.findViewById(R.id.layout_following)).setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ListView list = (ListView) view.findViewById(android.R.id.list);
        mProfileView = inflater.inflate(R.layout.view_user_profile, list, false);
        list.addHeaderView(mProfileView);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.layout_followers:
                intent = new Intent(getActivity(), MainActivity.class);
                intent.setAction(MainActivity.ACTION_VIEW_FOLLOWERS);
                intent.putExtra(MainActivity.EXTRA_USER_ID, mUser.getId());
                break;
            case R.id.layout_following:
                intent = new Intent(getActivity(), MainActivity.class);
                intent.setAction(MainActivity.ACTION_VIEW_FOLLOWING);
                intent.putExtra(MainActivity.EXTRA_USER_ID, mUser.getId());
                break;
        }
        if(intent != null) {
            startActivity(intent);
        }
    }
}
