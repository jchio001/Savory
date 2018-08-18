package com.savory.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class EditableRatingView extends RatingView {

    public EditableRatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        OnClickListener onImageClicked = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = indexOfChild(v);
                setRating(index + 1);
            }
        };

        for (int i = 0; i < 5 ; ++i) {
            imageViews[i].setOnClickListener(onImageClicked);
        }
    }
}
