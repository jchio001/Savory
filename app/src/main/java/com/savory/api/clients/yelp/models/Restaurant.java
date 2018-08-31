package com.savory.api.clients.yelp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Parcelable {

    @Json(name = "alias")
    private String id;

    private String name;

    @Json(name = "image_url")
    private String imageUrl;

    @Json(name = "display_phone")
    private String phoneNumber;

    private Coordinates coordinates;

    public static class Coordinates implements Parcelable {

        private double latitude;

        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        protected Coordinates(Parcel in) {
            latitude = in.readDouble();
            longitude = in.readDouble();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(latitude);
            dest.writeDouble(longitude);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Coordinates> CREATOR = new Parcelable.Creator<Coordinates>() {
            @Override
            public Coordinates createFromParcel(Parcel in) {
                return new Coordinates(in);
            }

            @Override
            public Coordinates[] newArray(int size) {
                return new Coordinates[size];
            }
        };
    }

    private Location location;

    public static class Location implements Parcelable {

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

        protected Location(Parcel in) {
            address1 = in.readString();
            city = in.readString();
            country = in.readString();
            state = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(address1);
            dest.writeString(city);
            dest.writeString(country);
            dest.writeString(state);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
            @Override
            public Location createFromParcel(Parcel in) {
                return new Location(in);
            }

            @Override
            public Location[] newArray(int size) {
                return new Location[size];
            }
        };
    }

    private List<Category> categories;

    public static class Category implements Parcelable {

        private String alias;

        private String title;

        String getAlias() {
            return alias;
        }

        String getTitle() {
            return title;
        }

        protected Category(Parcel in) {
            alias = in.readString();
            title = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(alias);
            dest.writeString(title);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
            @Override
            public Category createFromParcel(Parcel in) {
                return new Category(in);
            }

            @Override
            public Category[] newArray(int size) {
                return new Category[size];
            }
        };
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

    protected Restaurant(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        coordinates = (Coordinates) in.readValue(Coordinates.class.getClassLoader());
        location = (Location) in.readValue(Location.class.getClassLoader());
        if (in.readByte() == 0x01) {
            categories = new ArrayList<>();
            in.readList(categories, Category.class.getClassLoader());
        } else {
            categories = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeValue(coordinates);
        dest.writeValue(location);
        if (categories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categories);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}
