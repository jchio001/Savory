package com.savory.api.clients.googleplaces.models;

import com.squareup.moshi.Json;

import java.util.List;

public class Place {

    private Geometry geometry;

    private String icon;

    private String name;

    private List<Photo> photos;

    @Json(name = "place_id")
    private String placeId;

    private String vicinity;

    public Geometry getGeometry() {
        return geometry;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getVicinity() {
        return vicinity;
    }
}
