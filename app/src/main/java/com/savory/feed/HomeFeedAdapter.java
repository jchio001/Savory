package com.savory.feed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.savory.R;
import com.savory.api.clients.savory.mock.models.MockDishItem;
import com.savory.data.SharedPreferencesClient;
import com.savory.ui.StaticRatingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.FeedItemViewHolder> {

    private Picasso picasso;
    protected List<MockDishItem> feedItems = new ArrayList<>();
    protected SharedPreferencesClient sharedPreferencesClient;

    public HomeFeedAdapter(Context context) {
        this.picasso = Picasso.get();
        this.sharedPreferencesClient = new SharedPreferencesClient(context);
    }

    public void setFeedItems(List<MockDishItem> newFeedItems) {
        feedItems.clear();
        feedItems.addAll(newFeedItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    @NonNull
    @Override
    public FeedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_dish_cell, parent, false);
        return new FeedItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedItemViewHolder holder, int position) {
        holder.loadFeedItem(position);
    }

    class FeedItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.dish_info_text) TextView dishInfoText;
        @BindView(R.id.bookmark_toggle) TextView bookmarkToggle;
        @BindView(R.id.dish_rating_text) StaticRatingView dishRatingView;
        @BindView(R.id.dish_picture) ImageView dishPicture;
        @BindView(R.id.dish_description) TextView dishDescription;

        @BindColor(R.color.dark_gray) int darkGray;
        @BindColor(R.color.light_red) int lightRed;

        FeedItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void loadFeedItem(int position) {
            MockDishItem dish = feedItems.get(position);

            dishInfoText.setText(dish.getTitle());

            bookmarkToggle.clearAnimation();
            boolean isBookmarked = sharedPreferencesClient.hasUserBookmarkedDish(dish.getDishId());
            bookmarkToggle.setText(isBookmarked ? R.string.bookmark_filled_icon : R.string.bookmark_empty_icon);
            bookmarkToggle.setTextColor(isBookmarked ? lightRed : darkGray);

            if (dish.getRating() > 0) {
                dishRatingView.setRating(dish.getRating());
                dishRatingView.setVisibility(View.VISIBLE);
            } else {
                dishRatingView.setVisibility(View.GONE);
            }

            Picasso.get()
                    .load(dish.getPhotoUrl())
                    .fit()
                    .centerCrop()
                    .into(dishPicture);

            if (dish.getDescription().isEmpty()) {
                dishDescription.setVisibility(View.GONE);
            } else {
                dishDescription.setText(dish.getDescription());
                dishDescription.setVisibility(View.VISIBLE);
            }
        }
    }
}
