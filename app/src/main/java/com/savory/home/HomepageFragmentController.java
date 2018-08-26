package com.savory.home;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.savory.R;
import com.savory.account.AccountFragment;

public class HomepageFragmentController {

    private FragmentManager fragmentManager;
    private int containerId;
    private HomeFeedFragment homeFeedFragment;
    private SearchFragment searchFragment;
    private FavoritesFragment favoritesFragment;
    private AccountFragment accountFragment;
    @IdRes private int currentViewId;

    public HomepageFragmentController(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.homeFeedFragment = HomeFeedFragment.newInstance();
        this.searchFragment = SearchFragment.newInstance();
        this.favoritesFragment = FavoritesFragment.newInstance();
        this.accountFragment = AccountFragment.newInstance();
    }

    public void onNavItemSelected(@IdRes int viewId) {
        if (currentViewId == viewId) {
            return;
        }

        currentViewId = viewId;
        switch (viewId) {
            case R.id.home:
                swapInFragment(homeFeedFragment);
                break;
            case R.id.search:
                swapInFragment(searchFragment);
                break;
            case R.id.favorites:
                swapInFragment(favoritesFragment);
                break;
            case R.id.profile:
                swapInFragment(accountFragment);
                break;
        }
    }

    /** Called by the app upon start up to load the home fragment */
    public void loadHome() {
        currentViewId = R.id.home;
        swapInFragment(homeFeedFragment);
    }

    private void swapInFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(containerId, fragment).commit();
    }
}
