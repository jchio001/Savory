package com.savory.restaurant;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.savory.R;
import com.savory.api.clients.yelp.YelpRestaurantClient;
import com.savory.api.clients.yelp.models.Restaurant;
import com.savory.api.clients.yelp.models.RestaurantSearchResults;
import com.savory.location.LocationManager;
import com.savory.ui.SimpleItemDividerDecoration;
import com.savory.ui.YelpRestaurantSearchAdapter;
import com.savory.utils.Constants;
import com.savory.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class RestaurantPickerActivity extends AppCompatActivity {

    @BindView(R.id.yelp_restaurant_parent) View parent;
    @BindView(R.id.search_input) EditText searchInput;
    @BindView(R.id.clear_search) View clearSearch;
    @BindView(R.id.restaurant_search_skeleton) View loadingView;
    @BindView(R.id.no_restaurant_results) View noResults;
    @BindView(R.id.places_list) RecyclerView placesList;
    @BindView(R.id.set_location) FloatingActionButton setLocation;

    protected YelpRestaurantSearchAdapter placesAdapter;
    private LocationManager locationManager;
    protected boolean denialLock;
    protected String currentLocation;
    private YelpRestaurantClient yelpRestaurantClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_picker);
        ButterKnife.bind(this);

        placesAdapter = new YelpRestaurantSearchAdapter(this, placeChoiceListener);
        placesList.setAdapter(placesAdapter);
        placesList.addItemDecoration(new SimpleItemDividerDecoration(this));

        // When the user is scrolling to browse options, close the soft keyboard
        placesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    UIUtils.hideKeyboard(RestaurantPickerActivity.this);

                    // Stop the EditText cursor from blinking
                    parent.requestFocus();
                }
            }
        });

        locationManager = new LocationManager(locationListener, this);
        yelpRestaurantClient = YelpRestaurantClient.get();
        yelpRestaurantClient.setListener(placeFetchListener);

        setLocation.setImageDrawable(new IconDrawable(
                this,
                IoniconsIcons.ion_android_map).colorRes(R.color.white));
    }

    protected void scrollToTopOfResults() {
        placesList.getLayoutManager().scrollToPosition(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Run this here instead of onCreate() to cover the case where they return from turning on location
        if (currentLocation == null && !denialLock) {
            locationManager.fetchCurrentLocation();
        }
    }

    @OnTextChanged(value = R.id.search_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        placesList.setVisibility(View.GONE);
        noResults.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        if (currentLocation != null) {
            fetchRestaurants();
        }

        if (input.length() == 0) {
            clearSearch.setVisibility(View.GONE);
        } else {
            clearSearch.setVisibility(View.VISIBLE);
        }
    }

    /** Fetches restaurants with the current location and search input */
    protected void fetchRestaurants() {
        yelpRestaurantClient.getRestaurant(searchInput.getText().toString(), currentLocation);
    }

    @OnClick(R.id.clear_search)
    public void clearSearch() {
        searchInput.setText("");
    }

    @OnClick(R.id.back_button)
    public void goBack() {
        finish();
    }

    private final LocationManager.Listener locationListener = new LocationManager.Listener() {
        @Override
        public void onServicesOrPermissionChoice() {
            denialLock = false;
        }

        @Override
        public void onLocationFetched(String location) {
            currentLocation = location;
            fetchRestaurants();
        }
    };

    private final YelpRestaurantClient.Listener placeFetchListener = new YelpRestaurantClient.Listener() {
        @Override
        public void onRestaurantFetched(RestaurantSearchResults results) {
            List<Restaurant> restaurants = results.getRestaurants();

            loadingView.setVisibility(View.GONE);
            if (restaurants.isEmpty()) {
                noResults.setVisibility(View.VISIBLE);
            } else {
                placesAdapter.setRestaurants(restaurants);
                placesList.setVisibility(View.VISIBLE);
                scrollToTopOfResults();
            }
        }

        @Override
        public void onRestaurantFetchFail() {
            // TODO: Change the UI here, preferably with a CTA to refetch
        }
    };

    @OnClick(R.id.set_location)
    public void setLocation() {
        locationManager.showLocationForm();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults) {
        if (requestCode != LocationManager.LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        // No need to check if the location permission has been granted because of the onResume() block
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            denialLock = true;
            locationManager.showLocationPermissionDialog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != LocationManager.LOCATION_SERVICES_CODE) {
            return;
        }
        if (resultCode == RESULT_OK) {
            UIUtils.showLongToast(R.string.location_services_on, this);
            locationManager.fetchAutomaticLocation();
        } else {
            denialLock = true;
            locationManager.showLocationDenialDialog();
        }
    }

    private final YelpRestaurantSearchAdapter.Listener placeChoiceListener = new YelpRestaurantSearchAdapter.Listener() {
        @Override
        public void onItemClick(Restaurant place) {
            Intent intent = new Intent();
            intent.putExtra(Constants.PLACE_KEY, place);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        yelpRestaurantClient.shutdown();
    }
}
