package com.pepabo.jodo.jodoroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Follow;
import com.pepabo.jodo.jodoroid.models.Micropost;
import com.pepabo.jodo.jodoroid.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class UserProfileFragment extends MicropostListFragment
        implements View.OnClickListener, RefreshableView<User> {
    private static final String ARG_USER_ID = "user_id";
    private long mUserId;

    private boolean following;
    private APIService mAPIService;
    private Picasso mPicasso;
    private UserProfilePresenter mPresenter;

    @Bind(R.id.layout_followers)
    View followersLayoutView;

    @Bind(R.id.layout_following)
    View followingLayoutView;

    @Bind(R.id.button_follow_unfollow)
    Button followUnfollowButton;

    @Bind(R.id.textView_user_name)
    TextView userNameView;

    @Bind(R.id.textView_followers)
    TextView followersView;

    @Bind(R.id.textView_following)
    TextView followingView;

    @Bind(R.id.imageView_user_avatar)
    ImageView userAvatarView;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mAPIService = ((JodoroidApplication) activity.getApplication()).getAPIService();
        mPicasso = ((JodoroidApplication) activity.getApplication()).getPicasso();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserId = getArguments().getLong(ARG_USER_ID);

        mPresenter = new UserProfilePresenter(mAPIService, mUserId);
    }

    @Override
    public void onActivityCreated(Bundle SavedInstanveState) {
        super.onActivityCreated(SavedInstanveState);

        if (JodoAccount.isMe(getActivity().getApplicationContext(), mUserId)) {
            followUnfollowButton.setVisibility(View.GONE);
        } else {
            loadFollow();
        }

        mPresenter.setView(this);
        mPresenter.refresh();

        followUnfollowButton.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        mPresenter.setView(null);
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        mPresenter.refresh();
    }

    @Override
    public void onNextModel(User user) {
        setProfile(user);
        setItems(user.getMicroposts());
    }

    @Override
    public void onMoreModel(User user) {
        setProfile(user);
        addItems(user.getMicroposts());
    }

    private void setProfile(User user) {
        userNameView.setText(user.getName());
        followersView.setText(Long.toString(user.getFollowersCount()));
        followingView.setText(Long.toString(user.getFollowingCount()));
        mPicasso.load(user.getAvatarUrl()).fit().into(userAvatarView);

        followingLayoutView.setOnClickListener(this);
        followersLayoutView.setOnClickListener(this);
    }

    private void loadFollow() {
        mAPIService
                .fetchFollow(mUserId)
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
                .followUser(mUserId, "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        loadFollow();
                        mPresenter.refresh();
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
                .unfollowUser(mUserId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        loadFollow();
                        mPresenter.refresh();
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
        if (following) {
            followUnfollowButton.setText(getText(R.string.action_unfollow));
        } else {
            followUnfollowButton.setText(getText(R.string.action_follow));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        final ListView list = (ListView) view.findViewById(android.R.id.list);
        final View header = inflater.inflate(R.layout.view_user_profile, list, false);
        list.addHeaderView(header);
        ButterKnife.bind(this, header);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.layout_followers:
                intent = new Intent(getActivity(), MainActivity.class);
                intent.setAction(MainActivity.ACTION_VIEW_FOLLOWERS);
                intent.putExtra(MainActivity.EXTRA_USER_ID, mUserId);
                break;
            case R.id.layout_following:
                intent = new Intent(getActivity(), MainActivity.class);
                intent.setAction(MainActivity.ACTION_VIEW_FOLLOWING);
                intent.putExtra(MainActivity.EXTRA_USER_ID, mUserId);
                break;
            case R.id.button_follow_unfollow:
                if (following) {
                    unfollowUser();
                } else {
                    followUser();
                }
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onLoadError(Throwable e) {
        Toast.makeText(getActivity(),
                getString(R.string.toast_load_failure),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onLoadNextPage() {
        mPresenter.onLoadNextPage();
    }
}
