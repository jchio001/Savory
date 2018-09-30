package com.savory.api.clients.yelp;

import android.support.annotation.NonNull;

import com.savory.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class YelpAuthInterceptor implements Interceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer " + BuildConfig.YELP_API_KEY;

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request authorizedRequest = originalRequest.newBuilder()
                .header(AUTHORIZATION, BEARER)
                .build();
        return chain.proceed(authorizedRequest);
    }
}
