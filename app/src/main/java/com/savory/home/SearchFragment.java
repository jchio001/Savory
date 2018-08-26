package com.savory.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savory.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchFragment extends Fragment {

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
