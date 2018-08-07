package com.savory.photos;

import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.savory.api.models.Photo;
import com.squareup.picasso.Picasso;

public class PhotosCellViewHolder {

    private ImageView[] imageViews = new ImageView[3];

    public PhotosCellViewHolder(View v) {
        ViewGroup viewGroup = (ViewGroup) v;

        for (int i = 0, j = 0, childCount = viewGroup.getChildCount(); i < childCount; ++i) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ImageView) {
                imageViews[j++] = (ImageView) child;
            }
        }
    }

    public void setContent(Photo... photos) {
        Picasso picasso = Picasso.get();
        for (int i = 0, len = photos.length; i < len; ++i) {
            picasso.load(photos[i].getPhotoUrl())
                .into(imageViews[i]);
        }
    }
}
