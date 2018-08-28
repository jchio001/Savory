package com.savory.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.savory.R;
import com.savory.api.clients.googleplaces.models.Photo;
import com.savory.api.clients.googleplaces.models.Place;
import com.savory.api.clients.googleplaces.models.Places;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesAdapter extends BaseAdapter {

    private Picasso picasso;
    private List<Place> places = new LinkedList<>();

    public PlacesAdapter() {
        this.picasso = Picasso.get();
    }

    public void setPlaces(Places newPlaces) {
        places.clear();
        places.addAll(newPlaces.getResults());
        notifyDataSetChanged();
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

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
        List<Photo> photos = place.getPhotos();

        String imageUrl = (photos != null && !photos.isEmpty()) ? photos.get(0).getPhotoUrl() :
                place.getIcon();

        picasso.load(imageUrl)
                .into(placesViewHolder.previewImage);

        placesViewHolder.nameTextView.setText(place.getName());
        placesViewHolder.addressTextView.setText(place.getVicinity());

        return convertView;
    }

    static class PlacesViewHolder {

        @BindView(R.id.preview_image) ImageView previewImage;
        @BindView(R.id.name_textview) TextView nameTextView;
        @BindView(R.id.address_textview) TextView addressTextView;

        PlacesViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
