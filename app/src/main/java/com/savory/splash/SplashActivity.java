package com.savory.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.savory.data.SharedPreferencesClient;
import com.savory.home.HomeActivity;
import com.savory.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kill activity if it's above an existing stack due to launcher bug
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(this);
        startActivity(new Intent(
                this,
                sharedPreferencesClient.isUserLoggedIn() ? HomeActivity.class : LoginActivity.class));
        finish();
    }
}
