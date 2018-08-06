package com.savory.api.models;

import com.squareup.moshi.Json;

public class Account {

    private int id;

    @Json(name = "first_name")
    private String firstName;

    @Json(name = "last_name")
    private String lastName;

    @Json(name = "profile_image")
    private String profileImage;

    @Json(name = "creation_date")
    private long creationDate;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getProfileImage() {
        return profileImage;
    }

    public long getCreationDate() {
        return creationDate;
    }
}
