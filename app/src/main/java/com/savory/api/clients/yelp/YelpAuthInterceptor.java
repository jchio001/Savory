package com.savory.api.clients.yelp;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class YelpAuthInterceptor implements Interceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    // TODO: Move this into gradle.properties
    private static final String YELP_API_KEY = "EPEw9zVUVZN8y6RagHiJC1Q-_HvGv_RZ" +
            "jmxoP03GAdJLNVl1zVSOlfFu9Ivfajcr7bWv3hAmfYD6W" +
            "gLudKx1GmRXnY9G5TzoPISkTEdyzr-PLgAKlQm5WxsMOUqHW3Yx";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        String bearerToken = BEARER_PREFIX + YELP_API_KEY;

        Request authorizedRequest = originalRequest.newBuilder()
                .header(AUTHORIZATION, bearerToken)
                .build();
        return chain.proceed(authorizedRequest);
    }
}
