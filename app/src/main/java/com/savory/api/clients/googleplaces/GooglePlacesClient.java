package com.savory.api.clients.googleplaces;

import android.support.annotation.NonNull;

import com.savory.BuildConfig;
import com.savory.api.clients.googleplaces.models.Places;
import com.savory.api.resources.RetrofitBuilderFactory;

import retrofit2.Call;

public class GooglePlacesClient {

    private static final String BASE_URL = "https://maps.googleapis.com/";
    private static final String GOOGLE_API_KEY = BuildConfig.GOOGLE_API_KEY;

    private static GooglePlacesClient instance;

    private GooglePlacesService googlePlacesService;

    public static GooglePlacesClient get() {
        if (instance == null) {
            synchronized (GooglePlacesClient.class) {
                if (instance == null) {
                    instance = new GooglePlacesClient();
                }
            }
        }

        return instance;
    }

    private GooglePlacesClient() {
        googlePlacesService = RetrofitBuilderFactory.createBase()
            .baseUrl(BASE_URL)
            .build()
            .create(GooglePlacesService.class);
    }

    public Call<Places> getPlaces(@NonNull String keyword, String location) {
        return googlePlacesService.getPlaces(GOOGLE_API_KEY, keyword, location);
    }
}
