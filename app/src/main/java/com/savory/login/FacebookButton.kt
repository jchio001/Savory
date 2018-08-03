package com.savory.login

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.facebook.FacebookCallback
import com.facebook.login.LoginResult
import com.savory.R

class FacebookButton : FrameLayout {

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        View.inflate(context, R.layout.button_facebook, this);
    }
}