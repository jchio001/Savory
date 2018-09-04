package com.savory.api.clients.savory.mock.models;

public class MockDishItem {

    private int dishId;
    private String name;
    private String photoUrl;
    private String description;
    private int rating;
    private int numLikes;
    private MockUser user;
    private MockRestaurant restaurant;

    public int getDishId() {
        return dishId;
    }

    public MockDishItem setDishId(int dishId) {
        this.dishId = dishId;
        return this;
    }

    public String getName() {
        return name;
    }

    public MockDishItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public MockDishItem setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public String getDescription() {
        return "\"" + description + "\"";
    }

    public MockDishItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getRating() {
        return rating;
    }

    public MockDishItem setRating(int rating) {
        this.rating = rating;
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

    public String getTitle() {
        return name + " @ " + restaurant.getName();
    }
}
