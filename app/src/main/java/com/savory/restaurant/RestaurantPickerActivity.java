package com.savory.restaurant;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.savory.location.LocationManager;
import com.savory.ui.PlacesAdapter;
import com.savory.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class RestaurantPickerActivity extends AppCompatActivity implements LocationManager.Listener {

    public static final String PLACE_KEY = "place";

    @BindView(R.id.search_input) EditText searchInput;
    @BindView(R.id.clear_search) View clearSearch;
    @BindView(R.id.places_listview) ListView placesListView;
    @BindView(R.id.set_location) FloatingActionButton setLocation;

    private PlacesAdapter placesAdapter;
    private LocationManager locationManager;
    private boolean denialLock;
    private String currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_picker);
        ButterKnife.bind(this);

        locationManager = new LocationManager(this, this);
        placesAdapter = new PlacesAdapter(Picasso.get(), GooglePlacesClient.get(),
            new PlacesAdapter.ErrorListener() {
                @Override
                public void onErrorReceived(Throwable t) {

                }
            });

        placesAdapter.query(PlacesAdapter.DEFAULT_KEYWORD);
        placesListView.setAdapter(placesAdapter);

        setLocation.setImageDrawable(new IconDrawable(
                this,
                IoniconsIcons.ion_android_map).colorRes(R.color.white));
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
        if (input.length() == 0) {
            clearSearch.setVisibility(View.GONE);
        } else {
            clearSearch.setVisibility(View.VISIBLE);
        }
    }

    /** Fetches restaurants with the current location and search input */
    private void fetchRestaurants() {
        // restClient.fetchRestaurants(searchInput.getText().toString(), currentLocation);
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

    @OnClick(R.id.set_location)
    public void setLocation() {
        locationManager.showLocationForm();
    }

    @Override
    public void onServicesOrPermissionChoice() {
        denialLock = false;
    }

    @Override
    public void onLocationFetched(String location) {
        currentLocation = location;
        fetchRestaurants();
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

    @OnItemClick(R.id.places_listview)
    public void onPlaceSelected(int position) {
        Intent intent = new Intent();
        intent.putExtra(PLACE_KEY, placesAdapter.getItem(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}
