package com.savory.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.savory.R;

public class ExitDishFormDialog {

    public interface Listener {
        void onExitConfirmed();
    }

    private MaterialDialog dialog;

    public ExitDishFormDialog(final Listener listener, Context context) {
        this.dialog = new MaterialDialog.Builder(context)
                .title(R.string.confirm_form_exit)
                .content(R.string.confirm_form_exit_body)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onExitConfirmed();
                    }
                })
                .build();
    }

    public void show() {
        dialog.show();
    }
}
