package com.savory.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.savory.R;
import com.savory.api.clients.googleplaces.GooglePlacesClient;
import com.savory.ui.PlacesAdapter;
import com.savory.ui.PlacesAdapter.ErrorListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedFragment extends Fragment {

    @BindView(R.id.places_listview) ListView placesListView;

    private PlacesAdapter placesAdapter;

    public FeedFragment() {
        placesAdapter = new PlacesAdapter(GooglePlacesClient.get(), new ErrorListener() {
            @Override
            public void onErrorReceived(Throwable t) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (placesAdapter.getCount() == 0) {
            placesAdapter.query(PlacesAdapter.DEFAULT_KEYWORD);
        }

        placesListView.setAdapter(placesAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        placesAdapter.cancelPendingRequest();
    }
}
