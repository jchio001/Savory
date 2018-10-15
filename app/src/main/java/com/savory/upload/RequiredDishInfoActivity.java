package com.savory.upload;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.savory.R;
import com.savory.api.clients.yelp.models.Restaurant;
import com.savory.dialogs.ExitDishFormDialog;
import com.savory.restaurant.RestaurantPickerActivity;
import com.savory.ui.StandardActivity;
import com.savory.utils.Constants;
import com.savory.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class RequiredDishInfoActivity extends StandardActivity {

    private static final int RESTAURANT_REQUEST_CODE = 1;
    private static final int COMPLETE_UPLOAD_CODE = 2;

    private static final float DISABLED_ALPHA = 0.25f;

    @BindView(R.id.focus_sink) View focusSink;
    @BindView(R.id.next) View nextButton;
    @BindView(R.id.preview_imageview) ImageView dishPreview;
    @BindView(R.id.dish_title_input) EditText dishTitleInput;
    @BindView(R.id.restaurant_picker_cta_text) View pickRestaurantCta;
    @BindView(R.id.yelp_restaurant_parent) View restaurantContainerView;
    @BindView(R.id.restaurant_thumbnail) ImageView restaurantThumbnailView;
    @BindView(R.id.restaurant_name) TextView restaurantNameView;
    @BindView(R.id.restaurant_address) TextView addressTextView;
    @BindView(R.id.restaurant_categories) TextView categoriesTextView;

    private Picasso picasso;
    private Drawable defaultRestaurantThumbnail;
    private @Nullable Restaurant restaurant;
    private ExitDishFormDialog exitDishFormDialog;
    private DishForUpload dishForUpload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.required_dish_info);
        ButterKnife.bind(this);

        exitDishFormDialog = new ExitDishFormDialog(exitListener, this);

        picasso = Picasso.get();
        String filePathToImage = getIntent().getStringExtra(Constants.PHOTO_FILE_PATH_KEY);
        picasso.load(Uri.parse(filePathToImage))
                .fit()
                .centerCrop()
                .into(dishPreview);

        dishForUpload = new DishForUpload(filePathToImage);

        defaultRestaurantThumbnail = new IconDrawable(
                this,
                IoniconsIcons.ion_android_restaurant).colorRes(R.color.dark_gray);
    }

    private void loadRestaurantInfo(Restaurant restaurant) {
        this.restaurant = restaurant;
        if (TextUtils.isEmpty(restaurant.getImageUrl())) {
            restaurantThumbnailView.setImageDrawable(defaultRestaurantThumbnail);
        } else {
            picasso.load(restaurant.getImageUrl())
                    .error(defaultRestaurantThumbnail)
                    .fit()
                    .centerCrop()
                    .into(restaurantThumbnailView);
        }

        restaurantNameView.setText(restaurant.getName());
        addressTextView.setText(restaurant.getLocation().getAddress());
        categoriesTextView.setText(restaurant.getCategoriesString());

        pickRestaurantCta.setVisibility(View.GONE);
        restaurantContainerView.setVisibility(View.VISIBLE);

        updateNextButtonEnabledState();
    }

    @OnTextChanged(value = R.id.dish_title_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged() {
        updateNextButtonEnabledState();
    }

    private void updateNextButtonEnabledState() {
        nextButton.setAlpha(isFormValid() ? 1.0f : DISABLED_ALPHA);
    }

    private boolean isFormValid() {
        return !dishTitleInput.getText().toString().trim().isEmpty() && restaurant != null;
    }

    @OnClick(R.id.next)
    public void onNextClicked() {
        if (verifyFormAndMaybeShowErrors()) {
            String title = dishTitleInput.getText().toString().trim();
            dishForUpload.setTitle(title);
            dishForUpload.setRestaurant(restaurant);
            Intent intent = new Intent(this, ExtraDishInfoActivity.class)
                    .putExtra(Constants.DISH_FOR_UPLOAD_EXTRA_KEY, dishForUpload);
            startActivityForResult(intent, COMPLETE_UPLOAD_CODE);
        }
    }

    private boolean verifyFormAndMaybeShowErrors() {
        if (dishTitleInput.getText().toString().trim().isEmpty()) {
            UIUtils.showLongToast(R.string.no_dish_title_error, this);
            return false;
        }
        if (restaurant == null) {
            UIUtils.showLongToast(R.string.no_dish_location_error, this);
            return false;
        }
        return true;
    }

    @OnEditorAction(R.id.dish_title_input)
    public boolean onEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            focusSink.requestFocus();
            UIUtils.hideKeyboard(this);
            return true;
        }
        return false;
    }

    @OnClick(R.id.back_button)
    public void onBackButtonPressed() {
        exitDishFormDialog.show();
    }

    @Override
    public void onBackPressed() {
        exitDishFormDialog.show();
    }

    private final ExitDishFormDialog.Listener exitListener = new ExitDishFormDialog.Listener() {
        @Override
        public void onExitConfirmed() {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESTAURANT_REQUEST_CODE) {
                Restaurant restaurant = data.getParcelableExtra(Constants.PLACE_KEY);
                loadRestaurantInfo(restaurant);
            } else if (requestCode == COMPLETE_UPLOAD_CODE) {
                finish();
            }
        }
    }

    @OnClick(R.id.pick_restaurant)
    public void onRestaurantSectionClicked() {
        focusSink.requestFocus();
        startActivityForResult(
                new Intent(this, RestaurantPickerActivity.class),
                RESTAURANT_REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
    }
}
