package com.savory.upload;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.savory.R;
import com.savory.ui.StandardActivity;
import com.savory.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExtraDishInfoActivity extends StandardActivity {

    @BindView(R.id.dish_thumbnail) ImageView dishThumbnail;
    @BindView(R.id.description_input) EditText descriptionInput;

    private DishForUpload dishForUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extra_dish_info_and_submit);
        ButterKnife.bind(this);

        dishForUpload = getIntent().getParcelableExtra(Constants.DISH_FOR_UPLOAD_EXTRA_KEY);
        Picasso.get()
                .load(Uri.parse(dishForUpload.getFileUriString()))
                .fit()
                .centerCrop()
                .into(dishThumbnail);
    }

    @OnClick(R.id.back_button)
    public void onBackButtonPressed() {
        finish();
    }

    @OnClick(R.id.share)
    public void share() {

    }
}
