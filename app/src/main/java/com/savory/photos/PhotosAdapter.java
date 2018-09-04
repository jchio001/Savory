package com.savory.photos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.savory.R;
import com.savory.api.clients.savory.models.Photo;
import com.savory.ui.PagingAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosAdapter extends PagingAdapter<Photo> {

    private static final SimpleDateFormat DATE_FORMAT =
        new SimpleDateFormat("EEEE, MMMM dd, yyyy - hh:mm a", Locale.US);

    private Picasso picasso = Picasso.get();

    public PhotosAdapter(int pageSize) {
        super(pageSize);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Photo getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @Nullable
    public Integer getLastId() {
        int currentSize = objects.size();
        return currentSize != 0 ? objects.get(currentSize - 1).getId() : null;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_photos_cell, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Photo photo = objects.get(position);

        viewHolder.photoTimestamp.setText(DATE_FORMAT.format(new Date(photo.getCreationDate())));

        picasso.load(photo.getPhotoUrl())
            .into(viewHolder.imageView);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.dish_name) TextView dishName;
        @BindView(R.id.photo_timestamp) TextView photoTimestamp;
        @BindView(R.id.imageview) ImageView imageView;
        @BindView(R.id.description) TextView description;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
