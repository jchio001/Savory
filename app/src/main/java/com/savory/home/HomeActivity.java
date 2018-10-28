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
import com.savory.ui.BottomNavigationViewBinder;
import com.savory.ui.SimpleBlockingProgressDialog;
import com.savory.ui.StandardActivity;
import com.savory.upload.PhotoTakerManager;
import com.savory.upload.RequiredDishInfoActivity;
import com.savory.utils.Constants;
import com.savory.utils.PermissionUtils;
import com.savory.utils.UIUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends StandardActivity implements PhotoTakerManager.Listener {

    @BindView(R.id.bottom_navigation) View bottomNavigation;
    @BindString(R.string.choose_image_from) String chooseImageFrom;

    private final BottomNavigationViewBinder.Listener bottomNavListener = new BottomNavigationViewBinder.Listener() {
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

    protected HomepageFragmentController navigationController;
    private PhotoTakerManager photoTakerManager;
    protected SimpleBlockingProgressDialog photoProcessingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kill activity if it's above an existing stack due to launcher bug
        Intent intent = getIntent();
        String action = intent != null ? intent.getAction() : null;
        if (!isTaskRoot() && intent != null && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && action != null && action.equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        navigationController = new HomepageFragmentController(getSupportFragmentManager(), R.id.container);
        new BottomNavigationViewBinder(bottomNavigation, bottomNavListener);
        navigationController.loadHome();

        photoProcessingDialog = new SimpleBlockingProgressDialog(this, R.string.processing_taken_photo);
        photoTakerManager = new PhotoTakerManager(this);

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
                            UIUtils.showLongToast(
                                    R.string.play_store_error,
                                    HomeActivity.this);
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
        Intent takePhotoIntent = photoTakerManager.getPhotoTakingIntent(this);
        if (takePhotoIntent == null) {
            UIUtils.showLongToast(R.string.take_photo_with_camera_failed, this);
        } else {
            startActivityForResult(takePhotoIntent, Constants.CAMERA_CODE);
        }
    }

    @Override
    public void onTakePhotoFailure() {
        UIUtils.showLongToast(R.string.take_photo_with_camera_failed, this);
    }

    @Override
    public void onTakePhotoSuccess(final Uri takenPhotoUri) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                photoProcessingDialog.dismiss();
                Intent cameraIntent = new Intent(HomeActivity.this, RequiredDishInfoActivity.class)
                        .putExtra(Constants.PHOTO_FILE_PATH_KEY, takenPhotoUri.toString());
                startActivityForResult(cameraIntent, Constants.UPLOAD_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            photoProcessingDialog.show();
            photoTakerManager.processTakenPhoto(this);
        }
        if (requestCode == Constants.UPLOAD_CODE && resultCode == Activity.RESULT_CANCELED) {
            photoTakerManager.deleteLastTakenPhoto();
        }
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
