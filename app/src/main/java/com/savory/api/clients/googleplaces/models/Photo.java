package com.savory.api.clients.googleplaces.models;

import com.squareup.moshi.Json;

public class Photo {

    @Json(name = "photo_reference")
    private String photoReference;

    public String getPhotoReference() {
        return photoReference;
    }
}
