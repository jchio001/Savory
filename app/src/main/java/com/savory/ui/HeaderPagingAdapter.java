package com.savory.ui;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public abstract class HeaderPagingAdapter<T, U> extends PagingAdapter<U> {

    private static final int PROGRESS_BAR_ID = -1;
    private static final int HEADER_ID = 1;
    private static final int OBJECT_ID = 2;

    protected T headerObject;

    public HeaderPagingAdapter(int pageSize) {
        super(pageSize);
    }

    public abstract void addHeader(T headerObject);

    @Override
    public final Object getItem(int position) {
        int count = getCount();
        if (position == 0) {
            return headerObject;
        } else if (1 <= position && position < count - 1) {
            return objects.get(position - 1);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public final int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_ID;
        } else if (1 <= position && position < getCount() - 1) {
            return OBJECT_ID;
        } else {
            return HeaderPagingOnScrollListener.PROGRESS_BAR_FOOTER_ID;
        }
    }

    @Override
    @NonNull
    public final View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case HEADER_ID:
                return renderHeaderView(convertView, parent);
            case OBJECT_ID:
                return renderObjectView(position, convertView, parent);
            default:
                return renderProgressBarView(convertView, parent);
        }
    }

    @NonNull
    public abstract View renderHeaderView(View convertView, ViewGroup parent);

    @NonNull
    public abstract View renderObjectView(int position, View convertView, ViewGroup parent);

    @NonNull
    public abstract View renderProgressBarView(View convertView, ViewGroup parent);
}
