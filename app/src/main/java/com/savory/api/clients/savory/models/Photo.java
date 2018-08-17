package com.savory.api.clients.savory.models;

import com.squareup.moshi.Json;

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
