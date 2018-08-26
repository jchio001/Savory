package com.savory.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.savory.R;
import com.savory.upload.UploadFragment;
import com.savory.utils.FileUtils;
import com.savory.utils.PermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 32482;
    private static final int CAMERA_INTENT_REQUEST_CODE = 5463;

    public static final String PHOTO_FILE_PATH_KEY = "file_path";

    @BindView(R.id.please_click) TextView pleaseClick;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        if (PermissionUtils.isPermissionGranted(Manifest.permission.CAMERA, this)) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                                              new String[]{Manifest.permission.CAMERA},
                                              CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pleaseClick.setVisibility(View.GONE);
                openCamera();
            } else {
                pleaseClick.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onPostResume();

        if (requestCode == CAMERA_INTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            String filePath = FileUtils.createImageFile(this,
                                                        (Bitmap) data.getExtras().get("data"));

            Bundle bundle = new Bundle();
            bundle.putString(PHOTO_FILE_PATH_KEY, filePath);

            UploadFragment uploadFragment = new UploadFragment();
            uploadFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                .replace(R.id.parent, uploadFragment)
                .commit();
        } else {
            finish();
        }
    }

    @OnClick(R.id.please_click)
    public void onClick() {
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.CAMERA},
                                          CAMERA_PERMISSION_REQUEST_CODE);
    }

    public void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, CAMERA_INTENT_REQUEST_CODE);
    }
}
