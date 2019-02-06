package com.ejiaah.gallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String KEY_SELECTED_PHOTOS = "KeySelectedPhotos";
    private static final int REQUEST_CODE_SHOW_SAVED_PHOTO_ALBUM = 1000;
    private static final int REQUEST_CODE_PERMISSION_STORAGE = 2000;

    private TextView selected_photo_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selected_photo_textView = findViewById(R.id.selected_photo_textView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SHOW_SAVED_PHOTO_ALBUM) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<Photo> selectedPhotoList = data.getParcelableArrayListExtra(KEY_SELECTED_PHOTOS);

                StringBuilder stringBuilder = new StringBuilder();
                for (Photo photo : selectedPhotoList) {
                    if (photo == null) {
                        continue;
                    }
                    stringBuilder.append(photo).append("\n").append("--------------------\n");
                }
                selected_photo_textView.setText(stringBuilder.toString());
            }
        } else {
            //Nothing
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission is required.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private boolean hasPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS isrequestPermissions an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        } else {
            // Permission has already been granted
            return true;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_STORAGE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_gallery_button: {
                if(hasPermission()) {
                    Intent intent = new Intent(this, GalleryActivity.class);
                    intent.putExtra(GalleryActivity.KEY_TYPE, GalleryActivity.GalleryType.IMAGE);
                    startActivityForResult(intent, REQUEST_CODE_SHOW_SAVED_PHOTO_ALBUM);
                } else {
                    requestPermission();
                }
                break;
            }
            case R.id.video_gallery_button: {
                if(hasPermission()) {
                    Intent intent = new Intent(this, GalleryActivity.class);
                    intent.putExtra(GalleryActivity.KEY_TYPE, GalleryActivity.GalleryType.VIDEO);
                    startActivityForResult(intent, REQUEST_CODE_SHOW_SAVED_PHOTO_ALBUM);
                } else {
                    requestPermission();
                }
                break;
            }
        }
    }
}
