package com.savory.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.savory.R;
import com.savory.utils.Constants;

public class StaticRatingView extends IconTextView {

    public StaticRatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRating(int rating) {
        Context context = getContext();
        StringBuilder ratingText = new StringBuilder();
        for (int i = 0; i < Constants.MAX_NUM_STARS; i++) {
            if (i < rating) {
                ratingText.append(context.getString(R.string.red_filled_star));
            } else {
                ratingText.append(context.getString(R.string.blank_star));
            }
        }
        setText(ratingText.toString());
    }
}
