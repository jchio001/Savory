package com.savory.api;

import com.savory.api.models.AccountInfo;
import com.savory.api.models.Photo;
import com.savory.api.models.SavoryToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * High level interface that represents interacting with our API's endpoints.
 */
public interface SavoryService {

    /**
     * Represents connecting with Facebook.
     */
    @GET("/connect")
    Call<SavoryToken> connectWithSocial(@Query("token") String socialPlatformToken);

    /**
     * Represents a user getting their own account information
     */
    @GET("/account/me")
    Call<AccountInfo> getMyAccountInfo(@Query("token") String savoryToken);

    /**
     * Represents getting a page of photos for a user's feed
     */
    @GET("/photos")
    Call<List<Photo>> getPhotos(@Header("Authorization") String savoryToken,
                                @Query("last_id") Integer lastId);

    /**
     * Represents getting a page of photos for a specific account
     */
    @GET("/account/photos/{user_id}")
    Call<List<Photo>> getAccountPhotos(@Field("user_id") int userId);

    /**
     * Represents an account getting a page of their own photos.
     */
    @GET("/account/me/photos")
    Call<List<Photo>> getMyPhotos(@Header("Authorization") String savoryToken,
                                  @Field("last_id") Integer lastId);
}
