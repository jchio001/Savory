package com.savory.login

import android.app.Activity
import android.content.Intent
import android.view.View
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class FacebookClient {

    protected val callbackManager: CallbackManager = CallbackManager.Factory.create()
    // As it turns out, lambdas that represent interfaces can generate synthetic methods
    protected val loginManager: LoginManager = LoginManager.getInstance()

    // I did not make this a primary constructor since Kotlin tries to be smart with primary
    // constructors and auto-generates a respective member variable, getters and setters for values
    // in the primary constructor. I don't need that since all I literally just want to do is jam a
    // single onClickListener to my button.
    constructor(facebookButton: FacebookButton) {
        facebookButton.setOnClickListener { _ ->
            loginManager.logInWithReadPermissions(facebookButton.context as Activity, permissions) }
    }

    fun registerCallback(callback: FacebookCallback<LoginResult>) {
        loginManager.registerCallback(callbackManager, callback)
    }

    fun onFacebookLoginResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    companion object Constants {
        // https://developers.facebook.com/docs/facebook-login/permissions
        // TODO: Add back in "user_friends" once there's an actual logo
        val permissions = arrayListOf("public_profile")
    }
}