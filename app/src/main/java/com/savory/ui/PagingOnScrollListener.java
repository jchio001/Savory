package com.savory.ui;

import android.support.annotation.NonNull;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.savory.account.AccountAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Generic {@link OnScrollListener} for paging. This class interacts with the view layer & the data
 * layer through a {@link PageSupplier}, a high level interface to abstract away:
 * - Fetching data from the network
 * - Propagating any errors to the view layer
 * This listener expects that the adapter of the ListView it's observing extends
 * {@link AbstractPagingAdapter} and does a hard cast based on this assumption. Any other adapter
 * will cause this class to crash the application.
 * @param <T>
 */
public class PagingOnScrollListener<T> implements OnScrollListener {

    public interface PageSupplier<S> {
        Call<List<S>> supplyPage();
        void onFailure(@NonNull Throwable throwable);
    }

    protected PageSupplier<T> supplier;

    protected Call<List<T>> currentPageCall = null;

    protected boolean isFetching = false;

    public PagingOnScrollListener(PageSupplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onScroll(@NonNull final AbsListView view,
                         int firstVisibleItem,
                         int visibleItemCount,
                         int totalItemCount) {
        if (totalItemCount != 0
            && isLastItemProgressBar(view, firstVisibleItem, visibleItemCount, totalItemCount)
            && !isFetching) {
            isFetching = true;
            supplier.supplyPage().enqueue(new Callback<List<T>>() {
                @Override
                public void onResponse(@NonNull Call<List<T>> call,
                                       @NonNull Response<List<T>> response) {
                    ((AbstractPagingAdapter<T>) view.getAdapter()).addPage(response.body());
                    isFetching = false;
                }

                @Override
                public void onFailure(@NonNull Call<List<T>> call,
                                      @NonNull Throwable t) {
                    supplier.onFailure(t);
                    isFetching = false;
                }
            });
        }
    }

    private boolean isLastItemProgressBar(@NonNull AbsListView view,
                                          int firstVisibleItem,
                                          int visibleItemCount,
                                          int totalItemCount) {
        int lastIndex = Math.min(firstVisibleItem + visibleItemCount, totalItemCount - 1);
        return view.getAdapter().getItemViewType(lastIndex) == 3;
    }

    public void cancelPendingPage() {
        Call<List<T>> currentPageCall = this.currentPageCall;
        if (currentPageCall != null) {
            currentPageCall.cancel();
        }
    }
}
