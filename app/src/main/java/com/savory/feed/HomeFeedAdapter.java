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
import com.savory.utils.AnimationUtils;
import com.savory.utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFeedAdapter extends RecyclerView.Adapter<HomeFeedAdapter.FeedItemViewHolder> {

    public interface Listener {
        void onGetItClicked(MockDishItem mockDishItem);
    }

    protected Listener listener;
    protected Picasso picasso;
    protected List<MockDishItem> feedItems = new ArrayList<>();
    protected SharedPreferencesClient sharedPreferencesClient;

    public HomeFeedAdapter(Listener listener, Context context) {
        this.listener = listener;
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

        @BindView(R.id.user_profile_picture) ImageView userProfilePicture;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.dish_info_text) TextView dishInfoText;
        @BindView(R.id.bookmark_toggle) TextView bookmarkToggle;
        @BindView(R.id.dish_rating_text) StaticRatingView dishRatingView;
        @BindView(R.id.dish_picture) ImageView dishPicture;
        @BindView(R.id.like_toggle) TextView likeToggle;
        @BindView(R.id.num_likes) TextView numLikesText;
        @BindView(R.id.dish_description) TextView dishDescription;

        @BindColor(R.color.dark_gray) int darkGray;
        @BindColor(R.color.light_red) int lightRed;
        @BindColor(R.color.white) int white;

        FeedItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void loadFeedItem(int position) {
            MockDishItem dish = feedItems.get(position);

            picasso.load(dish.getUser().getProfilePictureUrl())
                    .fit()
                    .centerCrop()
                    .into(userProfilePicture);

            userName.setText(dish.getUser().getName());
            dishInfoText.setText(dish.getTitle());

            bookmarkToggle.clearAnimation();
            boolean isBookmarked = sharedPreferencesClient.hasUserBookmarkedDish(dish.getDishId());
            bookmarkToggle.setText(isBookmarked ? R.string.bookmark_filled_icon : R.string.bookmark_empty_icon);

            if (dish.getRating() > 0) {
                dishRatingView.setRating(dish.getRating());
                dishRatingView.setVisibility(View.VISIBLE);
            } else {
                dishRatingView.setVisibility(View.GONE);
            }

            picasso.load(dish.getPhotoUrl())
                    .fit()
                    .centerCrop()
                    .into(dishPicture);

            boolean isLiked = sharedPreferencesClient.doesUserLikeDish(dish.getDishId());
            likeToggle.setText(isLiked ? R.string.heart_filled_icon : R.string.heart_empty_icon);
            likeToggle.setTextColor(isLiked ? lightRed : darkGray);
            setNumLikesText();

            if (dish.getDescription().isEmpty()) {
                dishDescription.setVisibility(View.GONE);
            } else {
                dishDescription.setText(dish.getDescription());
                dishDescription.setVisibility(View.VISIBLE);
            }
        }

        private void setNumLikesText() {
            Context context = numLikesText.getContext();
            int likesAmount = feedItems.get(getAdapterPosition()).getNumLikes();
            numLikesText.setText(context.getString(R.string.x_likes, likesAmount));
        }

        @OnClick(R.id.bookmark_toggle)
        public void toggleBookmark() {
            MockDishItem dish = feedItems.get(getAdapterPosition());
            boolean isBookmarked = sharedPreferencesClient.hasUserBookmarkedDish(dish.getDishId());
            if (isBookmarked) {
                sharedPreferencesClient.unbookmarkDish(dish.getDishId());
            } else {
                sharedPreferencesClient.bookmarkDish(dish.getDishId());
            }
            UIUtils.showShortToast(
                    isBookmarked ? R.string.removed_from_bookmarks : R.string.saved_to_bookmarks,
                    bookmarkToggle.getContext());
            AnimationUtils.animateFeedItemToggle(
                    bookmarkToggle,
                    !isBookmarked,
                    white,
                    white,
                    R.string.bookmark_filled_icon,
                    R.string.bookmark_empty_icon);
        }

        @OnClick(R.id.like_toggle)
        public void toggleLike() {
            MockDishItem dish = feedItems.get(getAdapterPosition());
            boolean isLiked = sharedPreferencesClient.doesUserLikeDish(dish.getDishId());
            if (isLiked) {
                sharedPreferencesClient.unlikeDish(dish.getDishId());
                dish.removeLike();
            } else {
                sharedPreferencesClient.likeDish(dish.getDishId());
                dish.addLike();
            }
            setNumLikesText();
            AnimationUtils.animateFeedItemToggle(
                    likeToggle,
                    !isLiked,
                    lightRed,
                    darkGray,
                    R.string.heart_filled_icon,
                    R.string.heart_empty_icon);
        }

        @OnClick(R.id.get_it)
        public void onGetItClicked() {
            MockDishItem dish = feedItems.get(getAdapterPosition());
            listener.onGetItClicked(dish);
        }
    }
}
