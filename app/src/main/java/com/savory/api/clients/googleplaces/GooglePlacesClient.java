package com.savory.api.clients.googleplaces;

import android.support.annotation.NonNull;

import com.savory.BuildConfig;
import com.savory.api.clients.googleplaces.models.NearbyPlaces;
import com.savory.api.resources.RetrofitBuilderFactory;
import com.savory.location.LocationManager;

import retrofit2.Call;

public class GooglePlacesClient {

    private static final String BASE_URL =
        "https://maps.googleapis.com/maps/api/place/findplacefromtext/output";

    private static final String GOOGLE_API_KEY = BuildConfig.GOOGLE_API_KEY;

    private static GooglePlacesClient instance;

    private GooglePlacesService googlePlacesService;

    private LocationManager locationManager = new LocationManager();

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
        googlePlacesService = RetrofitBuilderFactory.createBase(locationManager)
            .baseUrl(BASE_URL)
            .build()
            .create(GooglePlacesService.class);
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public Call<NearbyPlaces> getPlaces(@NonNull String keyword) {
        return googlePlacesService.getPlaces(GOOGLE_API_KEY, keyword);
    }
}
