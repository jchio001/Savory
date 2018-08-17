package com.savory.api.clients.googleplaces.models;

import com.squareup.moshi.Json;

import java.util.List;

public class Place {

    private Geometry geometry;

    private String name;

    private List<Void> photos;

    @Json(name = "place_id")
    private String placeId;

    private String vicinity;

    public Geometry getGeometry() {
        return geometry;
    }

    public String getName() {
        return name;
    }

    public List<Void> getPhotos() {
        return photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getVicinity() {
        return vicinity;
    }
}
