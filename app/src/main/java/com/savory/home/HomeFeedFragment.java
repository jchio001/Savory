package com.savory.home;

import android.support.v4.app.Fragment;

public class HomeFeedFragment extends Fragment{

    public static HomeFeedFragment newInstance() {
        HomeFeedFragment fragment = new HomeFeedFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }
}
