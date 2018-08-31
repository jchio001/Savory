package com.savory.upload;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.savory.R;
import com.savory.api.clients.yelp.models.Restaurant;
import com.savory.restaurant.RestaurantPickerActivity;
import com.savory.ui.StandardActivity;
import com.savory.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadDishActivity extends StandardActivity {

    private static final int REQUEST_CODE = 2343;

    @BindView(R.id.preview_imageview) ImageView dishPreview;
    @BindView(R.id.restaurant_picker_cta_text) View pickRestaurantCta;
    @BindView(R.id.yelp_restaurant_parent) View restaurantContainerView;
    @BindView(R.id.restaurant_thumbnail) ImageView restaurantThumbnailView;
    @BindView(R.id.restaurant_name) TextView restaurantNameView;
    @BindView(R.id.restaurant_address) TextView addressTextView;
    @BindView(R.id.restaurant_categories) TextView categoriesTextView;

    private Picasso picasso;
    private Drawable defaultRestaurantThumbnail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload);
        ButterKnife.bind(this);
        picasso = Picasso.get();

        String filePathToImage = getIntent().getStringExtra(Constants.PHOTO_FILE_PATH_KEY);
        picasso.load(filePathToImage)
                .fit()
                .centerCrop()
                .into(dishPreview);

        defaultRestaurantThumbnail = new IconDrawable(
                this,
                IoniconsIcons.ion_android_restaurant).colorRes(R.color.dark_gray);
    }

    private void loadRestaurantInfo(Restaurant restaurant) {
        picasso.load(restaurant.getImageUrl())
                .error(defaultRestaurantThumbnail)
                .fit()
                .centerCrop()
                .into(restaurantThumbnailView);

        restaurantNameView.setText(restaurant.getName());
        addressTextView.setText(restaurant.getLocation().getAddress());
        categoriesTextView.setText(restaurant.getCategoriesString());

        pickRestaurantCta.setVisibility(View.GONE);
        restaurantContainerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Restaurant restaurant = data.getParcelableExtra(Constants.PLACE_KEY);
            loadRestaurantInfo(restaurant);
        }
    }

    @OnClick(R.id.pick_restaurant)
    public void onRestaurantSectionClicked() {
        startActivityForResult(new Intent(this, RestaurantPickerActivity.class), REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
    }
}
