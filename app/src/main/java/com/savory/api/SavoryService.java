package com.savory.api;

import com.savory.api.models.AccountInfo;
import com.savory.api.models.SavoryToken;

import retrofit2.Call;
import retrofit2.http.GET;
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

    @GET("/me")
    Call<AccountInfo> getMyAccountInfo(@Query("token") String savoryToken);
}
