package com.pepabo.jodo.jodoroid;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Follow;
import com.pepabo.jodo.jodoroid.models.User;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;


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
    private long user_id;

    private User mUser;
    private View mProfileView;
    private boolean following;
    private APIService mAPIService;

    @Bind(R.id.button_follow_unfollow) Button follow_unfollowButton;

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

        mAPIService = ((JodoroidApplication) getActivity().getApplication()).getAPIService();
        user_id = getArguments().getLong(ARG_USER_ID);
    }

    @Override
    public void onActivityCreated(Bundle SavedInstanveState) {
        super.onActivityCreated(SavedInstanveState);

        if (getArguments() != null) {
            loadUserPage();
            if(JodoAccounts.isMe(getActivity().getApplicationContext(), user_id)) {
                follow_unfollowButton.setVisibility(View.GONE);

            } else {
                loadFollow();
            }
        }

        follow_unfollowButton.setOnClickListener(this);
    }

    private void loadUserPage() {
        mAPIService
                .fetchUser(user_id, 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        mUser = user;
                        setProfile(user);
                        setMicroposts(user.getMicroposts());
                    }
                });
    }

    private void setProfile(User user){
        final Picasso picasso = ((JodoroidApplication) getActivity().getApplication()).getPicasso();
        ((TextView) mProfileView.findViewById(R.id.textView_user_name)).setText(user.getName());
        ((TextView) mProfileView.findViewById(R.id.textView_followers))
                .setText(Long.toString(user.getFollowersCount()));
        ((TextView) mProfileView.findViewById(R.id.textView_following))
                .setText(Long.toString(user.getFollowingCount()));
        picasso.load(user.getAvatarUrl()).fit().into((ImageView) mProfileView.findViewById(R.id.imageView_user_avatar));

        ((View) mProfileView.findViewById(R.id.layout_followers)).setOnClickListener(this);
        ((View) mProfileView.findViewById(R.id.layout_following)).setOnClickListener(this);
    }

    private void loadFollow() {
        mAPIService
                .fetchFollow(user_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Follow>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Follow follow) {
                        following = follow.getFollowing();
                        changeButtonText();
                    }
                });
    }

    private void followUser() {
        mAPIService
                .followUser(user_id, "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        loadFollow();
                        loadUserPage();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

    private void unfollowUser() {
        mAPIService
                .unfollowUser(user_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        loadFollow();
                        loadUserPage();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

    private void changeButtonText() {
        if(following) {
            follow_unfollowButton.setText(getText(R.string.action_unfollow));
        } else {
            follow_unfollowButton.setText(getText(R.string.action_follow));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ListView list = (ListView) view.findViewById(android.R.id.list);
        mProfileView = inflater.inflate(R.layout.view_user_profile, list, false);
        list.addHeaderView(mProfileView);
        ButterKnife.bind(this, mProfileView);
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
            case R.id.button_follow_unfollow:
                if(following) {
                    unfollowUser();
                } else {
                    followUser();
                }
                break;
        }
        if(intent != null) {
            startActivity(intent);
        }
    }

}
