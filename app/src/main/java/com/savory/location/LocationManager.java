package com.savory.location;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * TODO: Make this manager actually get the user's location!
 */
public class LocationManager implements Interceptor {

    private static final String LOCATION = "location";

    private double lat;
    private double lng;

    public LocationManager() {
        this.lat = 37.5268368;
        this.lng = -121.9616358;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl httpUrl = request.url().newBuilder()
            .addQueryParameter(LOCATION, String.format(Locale.US, "%f,%f", lat, lng))
            .build();

        request = request.newBuilder()
            .url(httpUrl)
            .build();

        return chain.proceed(request);
    }
}
