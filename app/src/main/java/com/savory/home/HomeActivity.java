package com.savory.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.savory.R;
import com.savory.account.AccountFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.action_home) ImageView actionHome;
    @BindView(R.id.action_profile) ImageView actionProfile;

    private FragmentManager fragmentManager;
    private ActionBarManager actionBarManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        actionBarManager = new ActionBarManager(getSupportActionBar());

        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new FeedFragment())
                .addToBackStack("FeedFragment")
                .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    @OnClick(R.id.action_home)
    public void onActionHome() {
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (!(fragmentList.get(fragmentList.size() - 1) instanceof FeedFragment)) {
            fragmentManager.popBackStack();
        }
    }

    @OnClick(R.id.action_profile)
    public void onActionProfile() {
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (!(fragmentList.get(fragmentList.size() - 1) instanceof AccountFragment)) {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new AccountFragment())
                .addToBackStack("AccountFragment")
                .commit();
        }
    }
}
