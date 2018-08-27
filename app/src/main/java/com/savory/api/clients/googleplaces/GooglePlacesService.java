package com.savory.api.clients.googleplaces;

import android.support.annotation.NonNull;

import com.savory.api.clients.googleplaces.models.Places;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * High-level interface to abstract away the details for interacting with Google Places API.
 */
public interface GooglePlacesService {

    /**
     * Makes a request to Google Places API to retrieve a list of nearby restaurants.
     */
    @GET("maps/api/place/nearbysearch/json?rankby=distance&type=food,restaurant")
    Call<Places> getPlaces(@NonNull @Query("key") String apiKey,
                           @NonNull @Query("keyword") String keyword);
}
