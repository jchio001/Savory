package com.savory.upload;

import android.os.Parcel;
import android.os.Parcelable;

import com.savory.api.clients.yelp.models.Restaurant;

public class DishForUpload implements Parcelable {

    private String fileUriString;
    private String title;
    private Restaurant restaurant;
    private String description;
    private int rating;

    public DishForUpload(String fileUriString) {
        this.fileUriString = fileUriString;
    }

    public String getFileUriString() {
        return fileUriString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    protected DishForUpload(Parcel in) {
        fileUriString = in.readString();
        title = in.readString();
        restaurant = (Restaurant) in.readValue(Restaurant.class.getClassLoader());
        description = in.readString();
        rating = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileUriString);
        dest.writeString(title);
        dest.writeValue(restaurant);
        dest.writeString(description);
        dest.writeInt(rating);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DishForUpload> CREATOR = new Parcelable.Creator<DishForUpload>() {
        @Override
        public DishForUpload createFromParcel(Parcel in) {
            return new DishForUpload(in);
        }

        @Override
        public DishForUpload[] newArray(int size) {
            return new DishForUpload[size];
        }
    };
}
