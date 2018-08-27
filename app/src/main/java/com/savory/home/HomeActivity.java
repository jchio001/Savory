package com.savory.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.savory.R;
import com.savory.data.SharedPreferencesClient;
import com.savory.ui.BottomNavigationView;
import com.savory.ui.StandardActivity;
import com.savory.utils.Constants;
import com.savory.utils.PermissionUtils;
import com.savory.utils.UIUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends StandardActivity {

    @BindView(R.id.bottom_navigation) View bottomNavigation;
    @BindString(R.string.choose_image_from) String chooseImageFrom;

    private final BottomNavigationView.Listener bottomNavListener = new BottomNavigationView.Listener() {
        @Override
        public void onNavItemSelected(@IdRes int viewId) {
            UIUtils.hideKeyboard(HomeActivity.this);
            navigationController.onNavItemSelected(viewId);
        }

        @Override
        public void takePicture() {
            addWithCamera();
        }
    };

    private BottomNavigationView bottomNavigationView;
    protected HomepageFragmentController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kill activity if it's above an existing stack due to launcher bug
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        navigationController = new HomepageFragmentController(getSupportFragmentManager(), R.id.container);
        bottomNavigationView = new BottomNavigationView(bottomNavigation, bottomNavListener);
        navigationController.loadHome();

        SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(this);
        if (sharedPreferencesClient.shouldAskForRating()) {
            showRatingPrompt();
        }
    }

    private void showRatingPrompt() {
        new MaterialDialog.Builder(this)
                .content(R.string.please_rate)
                .negativeText(R.string.no_im_good)
                .positiveText(R.string.will_rate)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Uri uri =  Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (!(getPackageManager().queryIntentActivities(intent, 0).size() > 0)) {
                            UIUtils.showLongToast(R.string.play_store_error, HomeActivity.this);
                            return;
                        }
                        startActivity(intent);
                    }
                })
                .show();
    }

    /** Starts the flow to add a dish via the camera */
    protected void addWithCamera() {
        if (PermissionUtils.isPermissionGranted(Manifest.permission.CAMERA, this)) {
            startCameraPage();
        } else {
            PermissionUtils.requestPermission(this, Manifest.permission.CAMERA);
        }
    }

    private void startCameraPage() {
        // Let's make this not suck
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != Constants.CAMERA_CODE) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            // Open dish upload form
        }

        if (resultCode == Constants.DISH_ADDED) {
            // Tab to home feed fragment and have the list simulate pulling to refresh
            bottomNavigationView.onHomeClicked();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Camera permission granted
        startCameraPage();
    }
}
