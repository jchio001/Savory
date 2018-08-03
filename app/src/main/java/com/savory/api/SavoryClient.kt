package com.savory.api

import com.savory.api.models.SavoryToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SavoryClient {

    private val savoryService : SavoryService
    private val retrofit : Retrofit

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor();
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

        retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        savoryService = retrofit.create(SavoryService::class.java)
    }

    fun connect(socialPlatformToken : String) : Call<SavoryToken> {
        return savoryService.connectWithSocial(socialPlatformToken)
    }

    companion object {
        const val BASE_URL = "http://savory-backend.herokuapp.com"

        val instance = SavoryClient()

        fun get() : SavoryClient {
            return instance
        }
    }
}