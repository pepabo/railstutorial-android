package com.pepabo.jodo.jodoroid.models;

import java.util.List;

import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import rx.Observable;

public interface APIService {
    @POST("/login")
    @FormUrlEncoded
    Observable<Session> login(@Field("session[email]") String email,
                           @Field("session[password]") String password);

    @DELETE("/logout")
    Observable<Void> logout();

    @GET("/feed")
    Observable<List<Micropost>> fetchFeed();
}
