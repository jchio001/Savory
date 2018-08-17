package com.savory.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Collections;
import java.util.List;

public class FacebookClient {

    private static final List<String> FB_PERMISSIONS = Collections.singletonList("public_profile");

    private CallbackManager callbackManager = CallbackManager.Factory.create();
    protected LoginManager loginManager = LoginManager.getInstance();

    public void bind(@NonNull FacebookButton facebookButton) {
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager.logInWithReadPermissions((Activity) v.getContext(), FB_PERMISSIONS);
            }
        });
    }

    public void registerCallback(@NonNull FacebookCallback<LoginResult> callback) {
        loginManager.registerCallback(callbackManager, callback);
    }

    public void onLoginResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
