package com.savory.upload;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

import com.savory.R;
import com.savory.api.clients.googleplaces.models.Place;
import com.savory.restaurant.RestaurantPickerActivity;
import com.savory.ui.StandardActivity;
import com.savory.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadDishActivity extends StandardActivity {
    private static final int REQUEST_CODE = 2343;

    @BindView(R.id.preview_imageview) ImageView previewImageView;
    @BindView(R.id.restaurant_section) CardView restaurantSection;
    @BindView(R.id.restaurant_name) TextView restaurantName;

    @BindColor(R.color.grey) @ColorInt int grey;

    private Place place;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload);
        ButterKnife.bind(this);

        String filePathToImage = getIntent().getStringExtra(Constants.PHOTO_FILE_PATH_KEY);

        Picasso.get()
                .load(filePathToImage)
                .into(previewImageView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            place = data.getParcelableExtra(RestaurantPickerActivity.PLACE_KEY);
            restaurantName.setTextColor(grey);
            restaurantName.setText(place.getName());
        }
    }

    @OnClick(R.id.restaurant_section)
    public void onRestaurantSectionClicked() {
        startActivityForResult(new Intent(this, RestaurantPickerActivity.class), REQUEST_CODE);
    }
}
