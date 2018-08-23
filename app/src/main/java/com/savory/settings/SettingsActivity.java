package com.savory.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.savory.R;
import com.savory.data.SPClient;
import com.savory.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    private SPClient spClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        spClient = new SPClient(this);
    }

    @OnClick(R.id.logout)
    public void logout() {
        spClient.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }
}
