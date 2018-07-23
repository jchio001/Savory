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

    fun registerCallback(callback: FacebookCallback<LoginResult>) {
        loginManager.registerCallback(callbackManager, callback)
    }

    fun bindButton(button: View) {
        button.setOnClickListener { _ ->
            loginManager.logInWithReadPermissions(button.context as Activity, permissions) }
    }

    fun onResultReceived(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    companion object Constants {
        // https://developers.facebook.com/docs/facebook-login/permissions
        val permissions = arrayListOf("public_profile", "user_friends")
    }
}