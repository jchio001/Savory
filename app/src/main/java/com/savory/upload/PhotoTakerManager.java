package com.savory.upload;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import com.savory.utils.Constants;
import com.savory.utils.FileUtils;
import com.savory.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PhotoTakerManager {

    public interface Listener {
        void onTakePhotoFailure();

        void onTakePhotoSuccess(Uri takenPhotoUri);
    }

    protected Listener listener;
    private Handler backgroundHandler;
    protected File currentPhotoFile;
    protected Uri currentPhotoUri;

    public PhotoTakerManager(Listener listener) {
        this.listener = listener;
        HandlerThread handlerThread = new HandlerThread("Camera Photos Processor");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    @Nullable
    public Intent getPhotoTakingIntent(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) == null) {
            return null;
        }
        currentPhotoFile = FileUtils.createImageFile(context);
        if (currentPhotoFile != null) {
            currentPhotoUri = FileProvider.getUriForFile(
                    context,
                    Constants.FILE_PROVIDER_AUTHORITY,
                    currentPhotoFile);

            // Grant access to content URI so camera app doesn't crash
            List<ResolveInfo> resolvedIntentActivities = context.getPackageManager()
                    .queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                String packageName = resolvedIntentInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, currentPhotoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);

            return takePictureIntent;
        } else {
            return null;
        }
    }

    public void processTakenPhoto(final Context context) {
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                context.revokeUriPermission(
                        currentPhotoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    ImageUtils.rotateImageIfRequired(context, currentPhotoFile, currentPhotoUri);
                    listener.onTakePhotoSuccess(currentPhotoUri);
                } catch (IOException exception) {
                    listener.onTakePhotoFailure();
                }
            }
        });
    }

    public void deleteLastTakenPhoto() {
        FileUtils.deleteCameraImageWithUri(currentPhotoUri);
    }
}
