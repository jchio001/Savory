package com.savory.api.clients.yelp.models;

import android.text.TextUtils;

import com.squareup.moshi.Json;

import java.util.List;

public class Restaurant {

    @Json(name = "alias")
    private String id;

    private String name;

    @Json(name = "image_url")
    private String imageUrl;

    @Json(name = "display_phone")
    private String phoneNumber;

    private Coordinates coordinates;

    public static class Coordinates {

        private double latitude;

        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    private Location location;

    public static class Location {

        private String address1;

        private String city;

        @Json(name = "zip_code")
        private String zipCode;

        private String country;

        private String state;

        String getCity() {
            return city;
        }

        String getZipCode() {
            return zipCode;
        }

        String getCountry() {
            return country;
        }

        String getState() {
            return state;
        }

        public String getAddress() {
            StringBuilder address = new StringBuilder();
            if (!TextUtils.isEmpty(address1)) {
                address.append(address1).append(", ");
            }
            address.append(city);
            return address.toString();
        }
    }

    private List<Category> categories;

    public static class Category {

        private String alias;

        private String title;

        String getAlias() {
            return alias;
        }

        String getTitle() {
            return title;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Location getLocation() {
        return location;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getCategoriesString() {
        StringBuilder categoriesList = new StringBuilder();
        for (Category placeCategory : categories) {
            if (categoriesList.length() > 0) {
                categoriesList.append(", ");
            }
            categoriesList.append(placeCategory.getTitle());
        }
        return categoriesList.toString();
    }
}
