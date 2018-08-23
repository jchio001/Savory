package com.savory.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.savory.R;
import com.savory.api.clients.googleplaces.GooglePlacesClient;
import com.savory.ui.PlacesAdapter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class RestaurantPickerActivity extends AppCompatActivity {

    public static final String PLACE_KEY = "place";

    @BindView(R.id.places_listview) ListView placesListView;

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        placesAdapter.cancelPendingRequest();
    }

    @OnItemClick(R.id.places_listview)
    public void onPlaceSelected(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(PLACE_KEY, placesAdapter.getItem(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}
