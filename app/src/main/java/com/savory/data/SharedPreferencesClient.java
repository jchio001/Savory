package com.savory.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * Client that wraps around SharedPreferences. Activity/fragment code should rarely interface with
 * this client directly, and should instead interface with data clients that wrap around this.
 */
public class SharedPreferencesClient {

    private SharedPreferences sharedPreferences;

    private static final String TOKEN_KEY = "token";
    private static final String NUM_APP_OPENS_KEY = "numAppOpens";
    private static final int OPENS_BEFORE_RATING = 5;

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

    public boolean shouldAskForRating() {
        int currentAppOpens = sharedPreferences.getInt(NUM_APP_OPENS_KEY, 0);
        currentAppOpens++;
        sharedPreferences.edit().putInt(NUM_APP_OPENS_KEY, currentAppOpens).apply();
        return currentAppOpens == OPENS_BEFORE_RATING;
    }

    public void clear() {
        sharedPreferences.edit()
            .clear()
            .apply();
    }
}
