package com.savory.ui;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.savory.R;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomNavigationViewBinder {

    public interface Listener {
        void onNavItemSelected(@IdRes int viewId);

        void takePicture();
    }

    @BindView(R.id.home) TextView homeButton;
    @BindView(R.id.search) TextView searchButton;
    @BindView(R.id.favorites) TextView favoritesButton;
    @BindView(R.id.profile) TextView profileButton;
    @BindColor(R.color.dark_gray) int darkGray;
    @BindColor(R.color.app_red) int red;

    @NonNull private Listener listener;
    private TextView currentlySelected;

    public BottomNavigationViewBinder(View parent, @NonNull Listener listener) {
        ButterKnife.bind(this, parent);
        this.listener = listener;

        currentlySelected = homeButton;
        homeButton.setTextColor(red);
    }

    @OnClick(R.id.home)
    public void onHomeClicked() {
        if (currentlySelected == homeButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        currentlySelected = homeButton;
        homeButton.setTextColor(red);
        listener.onNavItemSelected(R.id.home);
    }

    @OnClick(R.id.search)
    public void onSearchClicked() {
        if (currentlySelected == searchButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        searchButton.setTextColor(red);
        currentlySelected = searchButton;
        listener.onNavItemSelected(R.id.search);
    }

    @OnClick(R.id.camera)
    public void takePicture() {
        listener.takePicture();
    }

    @OnClick(R.id.favorites)
    public void onFavoritesClicked() {
        if (currentlySelected == favoritesButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        favoritesButton.setTextColor(red);
        currentlySelected = favoritesButton;
        listener.onNavItemSelected(R.id.favorites);
    }

    @OnClick(R.id.profile)
    public void onProfileClicked() {
        if (currentlySelected == profileButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        profileButton.setTextColor(red);
        currentlySelected = profileButton;
        listener.onNavItemSelected(R.id.profile);
    }
}
