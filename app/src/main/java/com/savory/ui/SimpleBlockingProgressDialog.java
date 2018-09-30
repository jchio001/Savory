package com.savory.ui;

import android.content.Context;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;

public class SimpleBlockingProgressDialog {

    private MaterialDialog dialog;

    public SimpleBlockingProgressDialog(Context context, @StringRes int progressMessage) {
        dialog = new MaterialDialog.Builder(context)
                .progress(true, 0)
                .content(progressMessage)
                .cancelable(false)
                .build();
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
