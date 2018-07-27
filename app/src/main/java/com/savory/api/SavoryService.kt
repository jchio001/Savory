package com.savory.api

import retrofit2.Call
import retrofit2.http.Path

/**
 * High level interface that represents interacting with our API's endpoints.
 */
interface SavoryService {

    /**
     * Represents connecting with Facebook.
     */
    fun connectWithSocial(@Path("token") authToken : String) : Call<String>;
}