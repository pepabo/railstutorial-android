package com.pepabo.jodo.jodoroid.models;

import java.util.List;

import retrofit.http.Body;
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
    Observable<List<User>> fetchAllUsers(@Query("page") int page);

    @POST("/users")
    @FormUrlEncoded
    Observable<Void> signup(@Field("user[name]")     String name,
                            @Field("user[email]")    String email,
                            @Field("user[password]") String password);

    @GET("/users/{id}")
    Observable<User> fetchUser(@Path("id")    long userId,
                               @Query("page") int  page);

    @GET("/users/me")
    Observable<User> fetchMe(@Query("page") int page);

    @PATCH("/users/me")
    @FormUrlEncoded
    Observable<User> updateMe(@Field("user[name]")     String name,
                              @Field("user[email]")    String email,
                              @Field("user[password]") String password);

    @GET("/users/{id}/follow")
    Observable<Follow> fetchFollow(@Path("id") long userId);

    @POST("/users/{id}/follow")
    Observable<Void> followUser(@Path("id") long userId,
                                @Body       String dummy);

    @DELETE("/users/{id}/follow")
    Observable<Void> unfollowUser(@Path("id")    long userId);

    @GET("/users/{id}/following")
    Observable<List<User>> fetchFollowing(@Path("id")    long userId,
                                          @Query("page") int  page);

    @GET("/users/{id}/followers")
    Observable<List<User>> fetchFollowers(@Path("id")    long userId,
                                          @Query("page") int  page);

    @Multipart
    @POST("/microposts")
    Observable<Micropost> createMicropost(@Part("micropost[content]") String content,
                                          @Part("micropost[picture]") TypedFile picture);

    @DELETE("/microposts/{id}")
    Observable<Void> deleteMicropost(@Path("id") long micropostId);

    @GET("/feed")
    Observable<List<Micropost>> fetchFeed(@Query("page") int page);

    @POST("/password_resets")
    @FormUrlEncoded
    Observable<Void> requestPasswordReset(@Field("password_reset[email]") String email);

    @PUT("/password_resets/{id}")
    @FormUrlEncoded
    Observable<Session> resetPassword(@Path("id")              String token,
                                      @Query("email")          String email,
                                      @Field("user[password]") String password);

    @GET("/account_activations/{id}/edit")
    Observable<Session> activateAccount(@Path("id") String token,
                                        @Query("email") String email);

    @GET("/stardom")
    Observable<Stardom> checkStardom();
}
