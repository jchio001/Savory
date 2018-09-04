package com.savory.api.clients.savory.mock.models;

public class MockUser {

    private String name;
    private String profilePictureUrl;

    public String getName() {
        return name;
    }

    public MockUser setName(String name) {
        this.name = name;
        return this;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public MockUser setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
        return this;
    }
}
