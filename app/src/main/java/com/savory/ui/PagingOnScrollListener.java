package com.savory.ui;

import android.support.annotation.NonNull;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagingOnScrollListener<T> implements OnScrollListener {

    public interface PageSupplier<S> {
        Call<List<S>> supplyPage();
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

    @Override
    public void onScroll(@NonNull AbsListView view,
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
                    isFetching = false;
                }

                @Override
                public void onFailure(@NonNull Call<List<T>> call,
                                      @NonNull Throwable t) {
                    isFetching = false;
                }
            });
        }
    }

    private boolean isLastItemProgressBar(AbsListView view,
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
