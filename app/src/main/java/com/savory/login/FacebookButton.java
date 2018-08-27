package com.savory.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.savory.R;

public class FacebookButton extends FrameLayout {

    public FacebookButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.button_facebook, this);
    }
}
