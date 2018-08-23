package com.savory.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.savory.R;
import com.savory.account.AccountFragment;
import com.savory.api.clients.savory.SavoryClient;
import com.savory.data.SPClient;
import com.savory.photos.PhotosFeedFragment;
import com.savory.upload.UploadFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.action_home) FrameLayout actionHome;
    @BindView(R.id.action_profile) FrameLayout actionProfile;

    private FragmentManager fragmentManager;
    private ActionBarManager actionBarManager;

    private PhotosFeedFragment feedFragment;
    private UploadFragment uploadFragment;
    private AccountFragment accountFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        SavoryClient.get().setSavoryToken(new SPClient(this).retrieveSavoryToken());

        feedFragment = new PhotosFeedFragment();
        uploadFragment = new UploadFragment();
        accountFragment = new AccountFragment();

        actionBarManager = new ActionBarManager(getSupportActionBar());

        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, feedFragment)
                .addToBackStack("PhotosFeedFragment")
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
        if (!(fragmentList.get(fragmentList.size() - 1) instanceof PhotosFeedFragment)) {
            fragmentManager.popBackStack();
        }
    }

    @OnClick(R.id.action_camera)
    public void onActionCamera() {
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (!(fragmentList.get(fragmentList.size() - 1) instanceof UploadFragment)) {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, uploadFragment)
                .addToBackStack("UploadFragment")
                .commit();
        }
    }

    @OnClick(R.id.action_profile)
    public void onActionProfile() {
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (!(fragmentList.get(fragmentList.size() - 1) instanceof AccountFragment)) {
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, accountFragment)
                .addToBackStack("AccountFragment")
                .commit();
        }
    }
}
