package com.savory.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.savory.R;
import com.savory.data.SharedPreferencesClient;
import com.savory.ui.BottomNavigationView;
import com.savory.ui.StandardActivity;
import com.savory.upload.UploadDishActivity;
import com.savory.utils.Constants;
import com.savory.utils.FileUtils;
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
        Intent intent = getIntent();
        String action = intent != null ? intent.getAction() : null;
        if (!isTaskRoot() && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && action != null && action.equals(Intent.ACTION_MAIN)) {
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
            PermissionUtils.requestPermission(this, Manifest.permission.CAMERA, Constants.CAMERA_CODE);
        }
    }

    private void startCameraPage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) == null) {
            return;
        }
        startActivityForResult(takePictureIntent, Constants.CAMERA_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != Constants.CAMERA_CODE || resultCode != Activity.RESULT_OK) {
            return;
        }

        if (data.getExtras() == null) {
            UIUtils.showLongToast(R.string.camera_fail, this);
            return;
        }

        Bitmap imageBitmap = (Bitmap) data.getExtras().get(Constants.DATA_KEY);
        if (imageBitmap == null) {
            UIUtils.showLongToast(R.string.camera_fail, this);
            return;
        }
        String filePath = FileUtils.createImageFile(this, imageBitmap);
        Intent intent = new Intent(this, UploadDishActivity.class)
                .putExtra(Constants.PHOTO_FILE_PATH_KEY, filePath);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults) {
        if (requestCode != Constants.CAMERA_CODE
                || grantResults.length <= 0
                || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        // Camera permission granted
        startCameraPage();
    }
}
