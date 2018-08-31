package com.savory.api.clients.yelp;

import com.savory.api.clients.yelp.models.RestaurantSearchResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * High-level interface to abstract away the details for interacting with the Yelp API
 */
public interface YelpService {

    /**
     * Makes a request to Yelp API to retrieve a list of nearby restaurants.
     */
    @GET("v3/businesses/search")
    Call<RestaurantSearchResults> fetchRestaurants(@Query("term") String term,
                                                   @Query("location") String location,
                                                   @Query("limit") int limit,
                                                   @Query("sort_by") String sortBy);
}
