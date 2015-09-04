package com.pepabo.jodo.jodoroid;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.User;

import java.util.List;

import rx.Observable;

public class UserFollowersPresenter extends RefreshPresenter<List<User>> {
    public static final int TYPE_FOLLOWERS = 0;
    public static final int TYPE_FOLLOWING = 1;

    APIService mAPIService;
    int mType;
    long mUserId;

    public UserFollowersPresenter(RefreshableView<List<User>> view, APIService apiService, int type, long userId) {
        super(view);
        mAPIService = apiService;
        mType = type;
        mUserId = userId;
    }

    @Override
    protected Observable<List<User>> getObservable() {
        switch (mType){
            case TYPE_FOLLOWERS:
                return mAPIService.fetchFollowers(mUserId, 1);
            case TYPE_FOLLOWING:
                return mAPIService.fetchFollowing(mUserId, 1);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    protected Observable<List<User>> loadNextPage(int pageNumber) {
        switch (mType){
            case TYPE_FOLLOWERS:
                return mAPIService.fetchFollowers(mUserId, pageNumber);
            case TYPE_FOLLOWING:
                return mAPIService.fetchFollowing(mUserId, pageNumber);
            default:
                throw new IllegalArgumentException();
        }
    }
}
