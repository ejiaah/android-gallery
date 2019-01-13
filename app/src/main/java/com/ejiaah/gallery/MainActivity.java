package com.ejiaah.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String KEY_SELECTED_PHOTOS = "KeySelectedPhotos";
    private static final int REQUEST_CODE_SHOW_SAVED_PHOTO_ALBUM = 1000;

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_gallery_button: {
                Intent intent = new Intent(this, GalleryActivity.class);
                intent.putExtra(GalleryActivity.KEY_TYPE, GalleryActivity.GalleryType.IMAGE);
                startActivityForResult(intent, REQUEST_CODE_SHOW_SAVED_PHOTO_ALBUM);
                break;
            }
            case R.id.video_gallery_button: {
                Intent intent = new Intent(this, GalleryActivity.class);
                intent.putExtra(GalleryActivity.KEY_TYPE, GalleryActivity.GalleryType.VIDEO);
                startActivityForResult(intent, REQUEST_CODE_SHOW_SAVED_PHOTO_ALBUM);
                break;
            }
        }
    }
}
