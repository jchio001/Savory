package com.savory.api.clients.googleplaces.models;

import com.savory.BuildConfig;
import com.squareup.moshi.Json;

import java.util.Locale;

/**
 * Photo object retrieved from Google. Not to be confused with
 * {@link com.savory.api.clients.savory.models.Photo}.
 */
public class Photo {

    private static final String PHOTO_URL_TEMPLATE =
        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s";

    @Json(name = "photo_reference")
    private String photoReference;

    public String getPhotoReference() {
        return photoReference;
    }

    public String getPhotoUrl() {
        return String.format(Locale.US, PHOTO_URL_TEMPLATE,
            photoReference, BuildConfig.GOOGLE_API_KEY);
    }
}
