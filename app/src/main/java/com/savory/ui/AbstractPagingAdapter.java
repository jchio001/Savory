package com.savory.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Abstract adapter with some basic code to deal with pagination.
 * @param <T>
 */
public abstract class AbstractPagingAdapter<T> extends BaseAdapter {

    // Member variables are protected to give access to implementations of this class
    protected ArrayList<T> objects;
    protected boolean isNextPageAvailable = false;
    protected int pageSize;

    public AbstractPagingAdapter(int pageSize) {
        this.pageSize = pageSize;
        objects = new ArrayList<>(pageSize * 2);
    }

    public abstract void onFirstPageResponse(Response firstPageResponse);

    public final void addPage(@NonNull Response<List<T>> pageResponse) {
        if (pageResponse.isSuccessful()) {
            List<T> page = pageResponse.body();
            objects.addAll(page);
            isNextPageAvailable = (page.size() == pageSize);
            notifyDataSetChanged();
        } else {

        }
    }

    @Nullable
    public abstract Integer getLastId();
}
