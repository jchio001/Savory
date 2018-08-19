package com.savory.ui;

import android.support.annotation.NonNull;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract adapter with some basic code to deal with pagination.
 * @param <T>
 */
public abstract class AbstractPagingAdapter<T> extends BaseAdapter {

    // Member variables are protected to give access to implementations of this class
    protected ArrayList<T> objects;
    protected boolean isPageAvailable = false;
    protected int pageSize;

    public AbstractPagingAdapter(int pageSize) {
        this.pageSize = pageSize;
        objects = new ArrayList<>(pageSize * 2);
    }

    public final void addPage(@NonNull List<T> page) {
        objects.addAll(page);
        isPageAvailable = (page.size() == pageSize);
        notifyDataSetChanged();
    }

    public abstract int getLastId();
}
