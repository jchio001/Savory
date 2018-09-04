package com.savory.api.clients.savory.mock.models;

public class MockDishItem {

    private String photoUrl;
    private int numLikes;
    private MockUser user;
    private MockRestaurant restaurant;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public MockDishItem setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public MockDishItem setNumLikes(int numLikes) {
        this.numLikes = numLikes;
        return this;
    }

    public MockUser getUser() {
        return user;
    }

    public MockDishItem setUser(MockUser user) {
        this.user = user;
        return this;
    }

    public MockRestaurant getRestaurant() {
        return restaurant;
    }

    public MockDishItem setRestaurant(MockRestaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }
}
