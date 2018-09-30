package com.savory.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.savory.R;

public class UserAgreementDialog {

    public interface Listener {
        void onUserAgreementAccepted();
    }

    private MaterialDialog dialog;

    public UserAgreementDialog(Context context, final Listener listener) {
        dialog = new MaterialDialog.Builder(context)
                .title(R.string.user_agreement)
                .content(Html.fromHtml(context.getString(R.string.agreement_terms)))
                .positiveText(R.string.i_agree)
                .negativeText(R.string.i_disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onUserAgreementAccepted();
                    }
                })
                .cancelable(false)
                .build();
        TextView bodyTextView = dialog.getContentView();
        bodyTextView.setLinksClickable(true);
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void show() {
        dialog.show();
    }
}
