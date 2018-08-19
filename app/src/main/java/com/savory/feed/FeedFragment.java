package com.savory.feed;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.savory.R;
import com.savory.api.clients.savory.SavoryClient;
import com.savory.api.clients.savory.models.Photo;
import com.savory.data.SPClient;
import com.savory.photos.PhotosAdapter;
import com.savory.ui.PagingOnScrollListener;
import com.savory.ui.PagingOnScrollListener.PageSupplier;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {

    @BindView(R.id.feed_listview) ListView feedListView;

    protected PhotosAdapter photosAdapter = new PhotosAdapter(10);

    private SavoryClient savoryClient;
    protected String savoryToken;

    private Call<List<Photo>> firstPageCall;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (savoryToken == null) {
            SPClient spClient = new SPClient(getContext());
            savoryToken = spClient.retrieveSavoryToken();
        }

        final SavoryClient savoryClient = SavoryClient.get();
        this.savoryClient = savoryClient;

        PageSupplier<Photo> photoPageSupplier = new PageSupplier<Photo>() {
            @Override
            public Call<List<Photo>> supplyPage() {
                return savoryClient.getPageOfMyPhotos(savoryToken, photosAdapter.getLastId());
            }

            @Override
            public void onFailure(@NonNull Throwable throwable) {
            }
        };

        PagingOnScrollListener<Photo> pagingOnScrollListener =
            new PagingOnScrollListener<>(photoPageSupplier);

        feedListView.setAdapter(photosAdapter);
        feedListView.setOnScrollListener(pagingOnScrollListener);

        if (photosAdapter.getCount() == 0) {
            Call<List<Photo>> firstPageCall = savoryClient.getPageOfPhotos(savoryToken, null);
            this.firstPageCall = firstPageCall;
            firstPageCall.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(@NonNull Call<List<Photo>> call,
                                       @NonNull Response<List<Photo>> response) {
                    if (response.isSuccessful()) {
                        photosAdapter.addPage(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Photo>> call,
                                      @NonNull Throwable t) {
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Call<List<Photo>> firstPageCall = this.firstPageCall;
        if (firstPageCall != null) {
            firstPageCall.cancel();
        }
    }
}
