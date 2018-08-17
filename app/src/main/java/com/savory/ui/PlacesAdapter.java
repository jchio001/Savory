package com.savory.ui;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.savory.R;
import com.savory.api.clients.googleplaces.GooglePlacesClient;
import com.savory.api.clients.googleplaces.models.NearbyPlaces;
import com.savory.api.clients.googleplaces.models.Place;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesAdapter extends BaseAdapter {

    public interface ErrorListener {
        void onErrorReceived(Throwable t);
    }

    public static final String DEFAULT_KEYWORD = "food";

    private GooglePlacesClient googlePlacesClient;

    protected List<Place> places = new LinkedList<>();

    protected ErrorListener errorListener;

    private Callback<NearbyPlaces> nearbyPlacesCallback = new Callback<NearbyPlaces>() {
        @Override
        public void onResponse(@NonNull Call<NearbyPlaces> call,
            @NonNull Response<NearbyPlaces> response) {
            if (response.isSuccessful()) {
                places = response.body().getResults();
                notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(@NonNull Call<NearbyPlaces> call,
            @NonNull Throwable t) {
            errorListener.onErrorReceived(t);
        }
    };

    private Call<NearbyPlaces> nearbyPlacesCall;

    public PlacesAdapter(@NonNull GooglePlacesClient googlePlacesClient,
                         @NonNull final ErrorListener errorListener) {
        this.googlePlacesClient = googlePlacesClient;
        this.errorListener = errorListener;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Place getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // TODO: Create the image url & load it into preview Image
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlacesViewHolder placesViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_cell, parent, false);
            placesViewHolder = new PlacesViewHolder(convertView);
            convertView.setTag(placesViewHolder);
        } else {
            placesViewHolder = (PlacesViewHolder) convertView.getTag();
        }

        Place place = places.get(position);
        placesViewHolder.nameTextView.setText(place.getName());
        placesViewHolder.addressTextView.setText(place.getVicinity());

        return convertView;
    }

    public void query(@NonNull String keyword) {
        cancelPendingRequest();

        Call<NearbyPlaces> nearbyPlacesCall = googlePlacesClient.getPlaces(keyword);
        this.nearbyPlacesCall = nearbyPlacesCall;
        nearbyPlacesCall.enqueue(nearbyPlacesCallback);
    }

    public void cancelPendingRequest() {
        Call<NearbyPlaces> nearbyPlacesCall = this.nearbyPlacesCall;
        if (nearbyPlacesCall != null) {
            nearbyPlacesCall.cancel();
        }
    }

    static class PlacesViewHolder {

        @BindView(R.id.preview_image) ImageView previewImage;
        @BindView(R.id.name_textview) TextView nameTextView;
        @BindView(R.id.address_textview) TextView addressTextView;

        public PlacesViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
