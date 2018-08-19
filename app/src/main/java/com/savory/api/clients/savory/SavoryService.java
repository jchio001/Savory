package com.savory.api.clients.savory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.savory.api.clients.savory.models.AccountInfo;
import com.savory.api.clients.savory.models.Photo;
import com.savory.api.clients.savory.models.SavoryToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * High level interface that represents interacting with our API's endpoints.
 */
public interface SavoryService {

    /**
     * Represents connecting with Facebook.
     */
    @GET("connect")
    Call<SavoryToken> connectWithSocial(@NonNull @Query("savoryToken") String socialPlatformToken);

    /**
     * Represents a user getting their own account information
     */
    @GET("account/me")
    Call<AccountInfo> getMyAccountInfo(@NonNull @Header("Authorization") String savoryToken);

    /**
     * Represents getting a page of photos for a user's feed
     */
    @GET("following/photos")
    Call<List<Photo>> getPageOfPhotos(@NonNull @Header("Authorization") String savoryToken,
                                      @Nullable @Query("last_id") Integer lastId);

    /**
     * Represents getting a page of photos for a specific account
     * THIS IS NOT YET IMPLEMENTED.
     */
    @GET("account/{account_id}/photos")
    Call<List<Photo>> getPageOfPhotosForAccount(@Path("account_id") int userId);

    /**
     * Represents an account getting a page of their own photos.
     */
    @GET("account/me/photos")
    Call<List<Photo>> getPageOfMyPhotos(@NonNull @Header("Authorization") String savoryToken,
                                        @Nullable @Query("last_id") Integer lastId);
}
