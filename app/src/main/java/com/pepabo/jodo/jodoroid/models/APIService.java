package com.pepabo.jodo.jodoroid.models;

import java.util.List;

import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import rx.Observable;

public interface APIService {
    @POST("/login")
    @FormUrlEncoded
    Observable<Session> login(@Field("session[email]")    String email,
                              @Field("session[password]") String password);
    
    @GET("/users")
    Observable<List<User>> fetchAllUsers(@Query("page") Integer page);

    @POST("/users")
    @FormUrlEncoded
    Observable<Void> signup(@Field("user[name]")     String name,
                            @Field("user[email]")    String email,
                            @Field("user[password]") String password);

    @GET("/users/{id}")
    Observable<User> fetchUser(@Path("id") Integer userId);

    @GET("/users/me")
    Observable<User> fetchMe();

    @PATCH("/users/me")
    Observable<User> updateMe();

    @POST("/users/{id}/follow")
    Observable<Void> followUser(@Path("id")    Integer userId,
                                @Query("page") Integer page);

    @DELETE("/users/{id}/follow")
    Observable<Void> unfollowUser(@Path("id")    Integer userId,
                                  @Query("page") Integer page);

    @GET("/users/{id}/following")
    Observable<List<User>> fetchFollowing(@Path("id")    Integer userId,
                                          @Query("page") Integer page);

    @GET("/users/{id}/followers")
    Observable<List<User>> fetchFollowers(@Path("id")    Integer userId,
                                          @Query("page") Integer page);

    @Multipart
    @POST("/microposts")
    Observable<Micropost> createMicropost(@Part("micropost[content]") String content,
                                          @Part("micropost[picture]") TypedFile picture);

    @DELETE("/microposts/{id}")
    Observable<Void> deleteMicropost(@Path("id") Integer micropostId);

    @GET("/feed")
    Observable<List<Micropost>> fetchFeed(@Query("page") Integer page);

    @POST("/password_resets")
    @FormUrlEncoded
    Observable<Void> requestPasswordReset(@Field("password_reset[email]") String email);

    @PUT("/password_resets")
    @FormUrlEncoded
    Observable<Session> resetPassword(@Field("user[password]") String password,
                                      @Query("email")          String email);
}
