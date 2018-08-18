package com.savory.upload;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.savory.R;
import com.savory.api.clients.googleplaces.models.Place;
import com.savory.restaurant.RestaurantPickerActivity;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadFragment extends Fragment {

    private static final int REQUEST_CODE = 2343;

    @BindView(R.id.restaurant_section) CardView restaurantSection;
    @BindView(R.id.restaurant_name) TextView restaurantName;

    @BindColor(R.color.grey) @ColorInt int grey;

    private Place place;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
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
        startActivityForResult(
            new Intent(getContext(), RestaurantPickerActivity.class), REQUEST_CODE);
    }
}
