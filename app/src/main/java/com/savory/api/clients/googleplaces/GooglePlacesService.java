package com.savory.api.clients.googleplaces;

import com.savory.api.clients.googleplaces.models.NearbyPlaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * High-level interface to abstract away the details for interacting with Google Places API.
 */
public interface GooglePlacesService {

    /**
     * Makes a request to Google Places API to retrieve a list of nearby restaurants.
     */
    @GET("maps/api/place/nearbysearch/json?rankby=distance&type=food,restaurant")
    Call<NearbyPlaces> getPlaces(@Query("key") String apiKey,
                                 @Query("keyword") String keyword);
}
