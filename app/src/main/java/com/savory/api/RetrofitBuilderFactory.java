package com.savory.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitBuilderFactory {

    private static final Retrofit baseRetrofit;
    private static final String STUBBED_BASE_URL = "http://pleasereplace.thisbaseurl";

    static {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build();

        baseRetrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(STUBBED_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();
    }

    /**
     * Creates a new Retrofit.Builder() with everything but the base URL initialized properly. This
     * approach allows for the underlying OkHttpClient & MoshiConverterFactory to be pooled across
     * instances, which saves a good amount of memory.
     * @return A cookie cutter Retrofit.Builder() instance
     */
    public static Retrofit.Builder createBaseRetrofit() {
        return baseRetrofit.newBuilder();
    }

}
