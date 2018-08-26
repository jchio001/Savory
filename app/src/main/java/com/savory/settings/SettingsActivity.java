package com.savory.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.savory.R;
import com.savory.data.SharedPreferencesClient;
import com.savory.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferencesClient SharedPreferencesClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        SharedPreferencesClient = new SharedPreferencesClient(this);
    }

    @OnClick(R.id.logout)
    public void logout() {
        SharedPreferencesClient.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }
}
