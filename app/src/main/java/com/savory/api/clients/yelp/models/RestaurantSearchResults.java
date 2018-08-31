package com.savory.api.clients.yelp.models;

import com.squareup.moshi.Json;

import java.util.List;

public class RestaurantSearchResults {

    @Json(name = "businesses")
    private List<Restaurant> restaurants;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
