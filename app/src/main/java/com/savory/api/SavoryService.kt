package com.savory.api

import com.savory.api.models.SavoryToken
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * High level interface that represents interacting with our API's endpoints.
 */
interface SavoryService {

    /**
     * Represents connecting with Facebook.
     */
    @GET("/connect")
    fun connectWithSocial(@Query("token") authToken : String): Call<SavoryToken>
}