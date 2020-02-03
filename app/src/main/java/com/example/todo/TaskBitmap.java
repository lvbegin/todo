package com.example.todo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class TaskBitmap {
    static public Bitmap getBitmapOrDefault(Uri uri, Activity activity) {
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        } catch (IOException e) {
            bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher_background);
        }
        return bitmap;
    }
}
