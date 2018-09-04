package com.savory.ui;

import android.support.annotation.NonNull;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.savory.ui.HeaderPagingOnScrollListener.PROGRESS_BAR_FOOTER_ID;

/**
 * Generic {@link OnScrollListener} for paging. This class interacts with the view layer & the data
 * layer through a {@link PageSupplier}, a high level interface to abstract away:
 * - Fetching data from the network
 * - Propagating any errors to the view layer
 * This listener expects that the adapter of the ListView it's observing extends
 * {@link PagingAdapter} and does a hard cast based on this assumption. Any other adapter
 * will cause this class to crash the application.
 * @param <T>
 */
public class PagingOnScrollListener<T> implements OnScrollListener {

    /**
     * High level client that the HeaderPagingOnScrollListener interacts with to fetch pages & to
     * communicate back to the view layer.
     * @param <S>
     */
    public interface PageSupplier<S> {

        /**
         * High level method that represents making an API call for the next page. Exposing this as
         * a high level method minimizes the amount of pagination related details
         * HeaderPagingOnScrollListener has to worry about.
         * @return A call for the next page.
         */
        Call<List<S>> supplyPage();

        /**
         * Often times, when we have a list of data that doesn't have any data yet, we like to
         * display a different view while our initial data is loading. Exposing this method in the
         * PageSupplier interface allows a way for HeaderPagingOnScrollListener to communicate this
         * back to the view layer.
         */
        void onFirstPageLoaded();

        /**
         * Communicates an error back to the view. This may not be needed, and is most definitely
         * subject to removal (so keep the code in this method as decoupled as possible).
         * @param throwable A throwable that I presume has been thrown from somewhere.
         */
        void onFailure(@NonNull Throwable throwable);
    }

    protected PageSupplier<T> supplier;

    // NOTE: Even though we're passing in a call object directly (which implies that we're handing
    // this class a live request, a call object only triggers the moment a callback is attached.
    // This means that we don't have to deal with nasty race conditions since we can control when
    // the call starts processing by attaching a callback to it (ty Based God Jake Wharton).
    protected Call currentPageCall;

    protected boolean isFetching = false;

    public PagingOnScrollListener(PageSupplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onScroll(@NonNull AbsListView view,
                         int firstVisibleItem,
                         int visibleItemCount,
                         int totalItemCount) {
        final PagingAdapter<T> pagingAdapter = (PagingAdapter<T>) view.getAdapter();
        boolean isLastItemProgressBar = isLastItemProgressBar(
                pagingAdapter,
                firstVisibleItem,
                visibleItemCount,
                totalItemCount);
        if ((totalItemCount == 0 || isLastItemProgressBar) && !isFetching) {
            isFetching = true;

            currentPageCall = supplier.supplyPage();
            currentPageCall.enqueue(new Callback<List<T>>() {

                @Override
                public void onResponse(@NonNull Call<List<T>> call, @NonNull Response<List<T>> response) {
                    if (response.isSuccessful()) {
                        if (pagingAdapter.isEmpty()) {
                            supplier.onFirstPageLoaded();
                        }

                        pagingAdapter.addPage(response);
                    }

                    isFetching = false;
                }

                @Override
                public void onFailure(@NonNull Call<List<T>> call, @NonNull Throwable t) {
                    supplier.onFailure(t);
                    isFetching = false;
                }
            });
        }
    }

    private static boolean isLastItemProgressBar(
            @NonNull PagingAdapter pagingAdapter,
            int firstVisibleItem,
            int visibleItemCount,
            int totalItemCount) {
        int lastIndex = Math.min(firstVisibleItem + visibleItemCount, totalItemCount - 1);
        return pagingAdapter.getItemViewType(lastIndex) == PROGRESS_BAR_FOOTER_ID;
    }

    public final void cancelPendingPage() {
        Call currentPageCall = this.currentPageCall;
        if (currentPageCall != null) {
            currentPageCall.cancel();
        }

        isFetching = false;
    }
}
