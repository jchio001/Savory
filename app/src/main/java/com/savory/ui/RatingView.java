package com.savory.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.savory.R;

import butterknife.BindDrawable;
import butterknife.ButterKnife;

public class RatingView extends LinearLayout {

    protected ImageView[] imageViews = new ImageView[5];

    @BindDrawable(R.drawable.ic_star) Drawable star;
    @BindDrawable(R.drawable.ic_star_half) Drawable halfStar;
    @BindDrawable(R.drawable.ic_star_empty) Drawable emptyStar;

    public RatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_rating, this);
        ButterKnife.bind(this, this);

        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            imageViews[i] = (ImageView) getChildAt(i);
        }
    }

    public void setRating(double rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalStateException(String.format("Invalid rating %f", rating));
        }

        int roundedRating = (int) Math.floor(rating);
        for (int i = 0; i < roundedRating; ++i) {
            imageViews[i].setImageDrawable(star);
        }

        int startIndex = roundedRating;
        double remainder = rating - roundedRating;
        if (0.33 <= remainder && remainder <= 0.75) {
            imageViews[roundedRating].setImageDrawable(halfStar);
            ++startIndex;
        } else if (remainder > 0.75) {
            imageViews[roundedRating].setImageDrawable(star);
            ++startIndex;
        }

        for (int i = startIndex; i < 5; ++i) {
            imageViews[i].setImageDrawable(emptyStar);
        }
    }
}
