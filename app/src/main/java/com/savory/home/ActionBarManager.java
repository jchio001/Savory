package com.savory.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;

import com.data.SPClient;
import com.savory.R;
import com.savory.login.LoginActivity;
import com.savory.settings.SettingsActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActionBarManager {

    private ActionBar actionBar;

    public ActionBarManager(ActionBar actionBar) {
        this.actionBar = actionBar;
        actionBar.setCustomView(R.layout.action_bar_layout);
        actionBar.setDisplayShowCustomEnabled(true);
        ButterKnife.bind(this, actionBar.getCustomView());
    }

    @OnClick(R.id.action_settings)
    public void onActionSettings() {
        Context context = actionBar.getThemedContext();
        context.startActivity(new Intent(context, SettingsActivity.class));
    }
}
