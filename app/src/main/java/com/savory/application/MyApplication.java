package com.savory.application;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;

public final class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new IoniconsModule())
                .with(new FontAwesomeModule());
    }
}
