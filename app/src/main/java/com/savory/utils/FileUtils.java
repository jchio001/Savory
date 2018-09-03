package com.savory.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    // Need to prepend this to file path so Picasso can load them
    private static final String FILE_PREFIX = "file://";

    public static File createImageFile(Context context) {
        File imageFile;
        try {
            String imageFileName = String.valueOf(System.currentTimeMillis());
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException exception) {
            return null;
        }
        return imageFile;
    }

    @Nullable
    public static String getGalleryImagePath(Intent data, Context context) {
        Uri selectedImage = data.getData();
        if (selectedImage == null) {
            return null;
        }
        Cursor cursor = context.getContentResolver().query(
                selectedImage,
                new String[] {android.provider.MediaStore.Images.ImageColumns.DATA},
                null,
                null,
                null);

        if (cursor == null) {
            return null;
        }

        cursor.moveToFirst();

        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String selectedImagePath = cursor.getString(idx);
        cursor.close();

        return FILE_PREFIX + selectedImagePath;
    }
}
