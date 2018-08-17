package com.savory.api.clients.savory.models;

import com.squareup.moshi.Json;

/**
 * Photo object returned from our backend. Not be confused with
 * {@link com.savory.api.clients.googleplaces.models.Photo}.
 */
public class Photo {

    private int id;

    @Json(name = "account_id")
    private int accountId;

    @Json(name = "photo_url")
    private String photoUrl;

    @Json(name = "creation_date")
    private long creationDate;

    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public long getCreationDate() {
        return creationDate;
    }
}
