package com.savory.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.savory.R;
import com.savory.api.clients.googleplaces.GooglePlacesClient;
import com.savory.ui.PlacesAdapter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class RestaurantPickerActivity extends AppCompatActivity {

    public static final String PLACE_KEY = "place";

    @BindView(R.id.search_input) EditText searchInput;
    @BindView(R.id.clear_search) View clearSearch;
    @BindView(R.id.places_listview) ListView placesListView;
    @BindView(R.id.set_location) FloatingActionButton setLocation;

    private PlacesAdapter placesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_picker);
        ButterKnife.bind(this);

        placesAdapter = new PlacesAdapter(Picasso.get(), GooglePlacesClient.get(),
            new PlacesAdapter.ErrorListener() {
                @Override
                public void onErrorReceived(Throwable t) {

                }
            });

        placesAdapter.query(PlacesAdapter.DEFAULT_KEYWORD);
        placesListView.setAdapter(placesAdapter);

        setLocation.setImageDrawable(new IconDrawable(this, IoniconsIcons.ion_android_map).colorRes(R.color.white));
    }

    @OnTextChanged(value = R.id.search_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        if (input.length() == 0) {
            clearSearch.setVisibility(View.GONE);
        } else {
            clearSearch.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.clear_search)
    public void clearSearch() {
        searchInput.setText("");
    }

    @Override
    protected void onStop() {
        super.onStop();
        placesAdapter.cancelPendingRequest();
    }

    @OnClick(R.id.back_button)
    public void goBack() {
        finish();
    }

    @OnItemClick(R.id.places_listview)
    public void onPlaceSelected(int position) {
        Intent intent = new Intent();
        intent.putExtra(PLACE_KEY, placesAdapter.getItem(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}
