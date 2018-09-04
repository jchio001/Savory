package com.savory.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.savory.R;
import com.savory.api.clients.yelp.models.Restaurant;
import com.savory.ui.YelpRestaurantSearchAdapter.RestaurantViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YelpRestaurantSearchAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    public interface Listener {
        void onItemClick(Restaurant place);
    }

    private Picasso picasso;
    protected List<Restaurant> restaurants = new ArrayList<>();
    private Drawable defaultThumbnail;
    protected Listener listener;

    public YelpRestaurantSearchAdapter(Context context, Listener listener) {
        this.picasso = Picasso.get();
        this.listener = listener;
        defaultThumbnail = new IconDrawable(
                context,
                IoniconsIcons.ion_android_restaurant).colorRes(R.color.dark_gray);
    }

    public void setRestaurants(List<Restaurant> newRestaurants) {
        restaurants.clear();
        restaurants.addAll(newRestaurants);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_cell, parent, false);
        return new RestaurantViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant place = restaurants.get(position);

        String placeImageUrl = place.getImageUrl();
        if (!TextUtils.isEmpty(placeImageUrl)) {
            picasso.load(place.getImageUrl())
                .error(defaultThumbnail)
                .fit()
                .centerCrop()
                .into(holder.previewImage);
        } else {
            holder.previewImage.setImageDrawable(defaultThumbnail);
        }

        holder.nameTextView.setText(place.getName());
        holder.addressTextView.setText(place.getLocation().getAddress());
        holder.categoriesText.setText(place.getCategoriesString());
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.restaurant_thumbnail) ImageView previewImage;
        @BindView(R.id.restaurant_name) TextView nameTextView;
        @BindView(R.id.restaurant_address) TextView addressTextView;
        @BindView(R.id.restaurant_categories) TextView categoriesText;

        RestaurantViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.yelp_restaurant_parent)
        public void onClick() {
            listener.onItemClick(restaurants.get(getAdapterPosition()));
        }
    }
}
