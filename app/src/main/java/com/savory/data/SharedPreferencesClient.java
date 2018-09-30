package com.savory.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Client that wraps around SharedPreferences. Activity/fragment code should rarely interface with
 * this client directly, and should instead interface with data clients that wrap around this.
 */
public class SharedPreferencesClient {

    private SharedPreferences sharedPreferences;

    private static final String TOKEN_KEY = "token";
    private static final String NUM_APP_OPENS_KEY = "numAppOpens";
    private static final int OPENS_BEFORE_RATING = 5;

    private static final String BOOKMARKED_DISHES_KEY = "bookmarkedDishes";
    private static final String LIKED_DISHES_KEY = "likedDishes";

    public SharedPreferencesClient(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void persistSavoryToken(@NonNull String token) {
        sharedPreferences.edit()
            .putString(TOKEN_KEY, token)
            .apply();
    }

    public String retrieveSavoryToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public boolean isUserLoggedIn() {
        return retrieveSavoryToken() != null;
    }

    public boolean shouldAskForRating() {
        int currentAppOpens = sharedPreferences.getInt(NUM_APP_OPENS_KEY, 0);
        currentAppOpens++;
        sharedPreferences.edit().putInt(NUM_APP_OPENS_KEY, currentAppOpens).apply();
        return currentAppOpens == OPENS_BEFORE_RATING;
    }

    public void likeDish(int dishId) {
        Set<String> likedIds = new HashSet<>(sharedPreferences.getStringSet(
                LIKED_DISHES_KEY,
                new HashSet<String>()));
        likedIds.add(String.valueOf(dishId));
        sharedPreferences.edit().putStringSet(LIKED_DISHES_KEY, likedIds).apply();
    }

    public void unlikeDish(int dishId) {
        Set<String> likedIds = new HashSet<>(sharedPreferences.getStringSet(
                LIKED_DISHES_KEY,
                new HashSet<String>()));
        likedIds.remove(String.valueOf(dishId));
        sharedPreferences.edit().putStringSet(LIKED_DISHES_KEY, likedIds).apply();
    }

    public boolean doesUserLikeDish(int dishId) {
        Set<String> likedIds = sharedPreferences.getStringSet(LIKED_DISHES_KEY, new HashSet<String>());
        return likedIds.contains(String.valueOf(dishId));
    }

    public void bookmarkDish(int dishId) {
        Set<String> bookmarkedIds = new HashSet<>(sharedPreferences.getStringSet(
                BOOKMARKED_DISHES_KEY,
                new HashSet<String>()));
        bookmarkedIds.add(String.valueOf(dishId));
        sharedPreferences.edit().putStringSet(BOOKMARKED_DISHES_KEY, bookmarkedIds).apply();
    }

    public void unbookmarkDish(int dishId) {
        Set<String> bookmarkedIds = new HashSet<>(sharedPreferences.getStringSet(
                BOOKMARKED_DISHES_KEY,
                new HashSet<String>()));
        bookmarkedIds.remove(String.valueOf(dishId));
        sharedPreferences.edit().putStringSet(BOOKMARKED_DISHES_KEY, bookmarkedIds).apply();
    }

    public boolean hasUserBookmarkedDish(int dishId) {
        Set<String> bookmarkedIds = sharedPreferences.getStringSet(BOOKMARKED_DISHES_KEY, new HashSet<String>());
        return bookmarkedIds.contains(String.valueOf(dishId));
    }

    public void clear() {
        sharedPreferences.edit()
            .clear()
            .apply();
    }
}
