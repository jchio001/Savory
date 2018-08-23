package com.savory.ui;

import android.support.annotation.NonNull;
import android.widget.AbsListView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Generic OnScrollListener that handles populating a {@link HeaderPagingAdapter}.
 * @param <T> The class of the header
 * @param <U> The class of the generic page object
 */
public class HeaderPagingOnScrollListener<T, U> extends PagingOnScrollListener<U> {

    public static final int PROGRESS_BAR_FOOTER_ID = -1;

    public HeaderPagingOnScrollListener(@NonNull Call<T> firstPageCall,
                                        @NonNull PageSupplier<U> supplier) {
        super(supplier);
        this.currentPageCall = firstPageCall;
    }

    @Override
    public void onScrollStateChanged(@NonNull AbsListView view, int scrollState) {
    }

    // TODO: Fix up the call logic once this app is at the point where we care about managing calls
    // TODO: with respect to fragment lifecycle.
    @SuppressWarnings("unchecked")
    @Override
    public void onScroll(@NonNull final AbsListView view,
                         int firstVisibleItem,
                         int visibleItemCount,
                         int totalItemCount) {
        final HeaderPagingAdapter<T, U> headerPagingAdapter =
            (HeaderPagingAdapter<T, U>) view.getAdapter();

        if (totalItemCount == 0 && !isFetching) {
            isFetching = true;

            if (!currentPageCall.isExecuted()) {
                currentPageCall.enqueue(new Callback<T>() {

                    @Override
                    public void onResponse(@NonNull Call<T> call,
                                           @NonNull Response<T> response) {
                        headerPagingAdapter.addHeader(response.body());
                        supplier.onFirstPageLoaded();
                        isFetching = false;
                    }

                    @Override
                    public void onFailure(@NonNull Call call,
                                          @NonNull Throwable t) {
                        isFetching = false;
                    }
                });
            }
        } else {
            super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
