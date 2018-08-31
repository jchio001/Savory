package com.savory.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.savory.R;
import com.savory.api.clients.yelp.models.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    public interface Listener {
        void onItemClick(Restaurant place);
    }

    private Picasso picasso;
    private List<Restaurant> places = new LinkedList<>();
    private Drawable defaultThumbnail;
    private Listener listener;

    public PlacesAdapter(Context context, Listener listener) {
        this.picasso = Picasso.get();
        this.listener = listener;
        defaultThumbnail = new IconDrawable(
                context,
                IoniconsIcons.ion_android_restaurant).colorRes(R.color.dark_gray);
    }

    public void setPlaces(List<Restaurant> newPlaces) {
        places.clear();
        places.addAll(newPlaces);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_cell, parent, false);
        return new PlacesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {
        Restaurant place = places.get(position);

        picasso.load(place.getImageUrl())
                .error(defaultThumbnail)
                .fit()
                .centerCrop()
                .into(holder.previewImage);

        holder.nameTextView.setText(place.getName());
        holder.addressTextView.setText(place.getLocation().getAddress());
    }

    class PlacesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.restaurant_thumbnail) ImageView previewImage;
        @BindView(R.id.restaurant_name) TextView nameTextView;
        @BindView(R.id.restaurant_address) TextView addressTextView;

        PlacesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.parent)
        public void onClick() {
            listener.onItemClick(places.get(getAdapterPosition()));
        }
    }
}
