package com.savory.account;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.savory.R;
import com.savory.api.clients.savory.models.Account;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountInfoViewHolder {

    @BindView(R.id.account_image)
    ImageView accountImage;

    @BindView(R.id.account_name)
    TextView accountName;

    public AccountInfoViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void populate(@NonNull Account account) {
        accountName.setText(account.getFullName());
        Picasso.get()
            .load(account.getProfileImage())
            .into(accountImage);
    }
}
