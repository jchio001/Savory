package com.savory.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Client that wraps around SharedPreferences. Activity/fragment code should rarely interface with
 * this client directly, and should instead interface with data clients that wrap around this.
 */
public class SPClient {

    private SharedPreferences sharedPreferences;

    private static final String TOKEN = "TOKEN";

    public SPClient(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void persistSavoryToken(String token) {
        sharedPreferences.edit()
            .putString(TOKEN, token)
            .apply();
    }

    public String retrieveSavoryToken() {
        return sharedPreferences.getString(TOKEN, null);
    }

    public void clear() {
        sharedPreferences.edit()
            .clear()
            .apply();
    }
}
