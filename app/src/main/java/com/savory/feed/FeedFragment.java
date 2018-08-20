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
import com.savory.photos.PhotosAdapter;
import com.savory.ui.PagingOnScrollListener;
import com.savory.ui.PagingOnScrollListener.PageSupplier;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class FeedFragment extends Fragment {

    @BindView(R.id.feed_listview) ListView feedListView;

    protected PhotosAdapter photosAdapter;

    private SavoryClient savoryClient;
    protected String savoryToken;

    private PagingOnScrollListener<Photo> pagingOnScrollListener;

    public FeedFragment() {
        photosAdapter = new PhotosAdapter(10);

        final SavoryClient savoryClient = SavoryClient.get();
        this.savoryClient = SavoryClient.get();

        PageSupplier<Photo> photoPageSupplier = new PageSupplier<Photo>() {
            @Override
            public Call<List<Photo>> supplyPage() {
                return savoryClient.getPageOfPhotos(photosAdapter.getLastId());
            }

            @Override
            public void onFirstPageLoaded() {
            }

            @Override
            public void onFailure(@NonNull Throwable throwable) {
            }
        };

        pagingOnScrollListener = new PagingOnScrollListener<>(photoPageSupplier);
    }

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

        feedListView.setAdapter(photosAdapter);
        feedListView.setOnScrollListener(pagingOnScrollListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        pagingOnScrollListener.cancelPendingPage();
    }
}
