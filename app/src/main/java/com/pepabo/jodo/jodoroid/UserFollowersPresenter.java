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

    public UserFollowersPresenter(APIService apiService, ExpirationManager expirationManager, int type, long userId) {
        super(expirationManager);
        mAPIService = apiService;
        mType = type;
        mUserId = userId;
    }

    @Override
    protected Observable<List<User>> getObservable(int page) {
        switch (mType){
            case TYPE_FOLLOWERS:
                return mAPIService.fetchFollowers(mUserId, page);
            case TYPE_FOLLOWING:
                return mAPIService.fetchFollowing(mUserId, page);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isLast(List<User> users) {
        return users.size() == 0;
    }
}
