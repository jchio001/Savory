package com.savory.home;

import android.support.v4.app.Fragment;

public class FavoritesFragment extends Fragment {

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
}
