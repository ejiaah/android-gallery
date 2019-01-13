package com.ejiaah.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements GalleryInteractionListener {

    private static final String TAG = GalleryActivity.class.getSimpleName();

    public static final String KEY_TYPE = "KeyType";
    public static int LIMIT_SELECT_COUNT = 10;

    public interface GalleryType {
        int IMAGE = 0;
        int VIDEO = 1;
    }

    GalleryManager galleryManager;

    int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        type = getIntent().getIntExtra(KEY_TYPE, GalleryType.IMAGE);
        if (type == GalleryType.IMAGE) {
            LIMIT_SELECT_COUNT = 10;
        } else {
            LIMIT_SELECT_COUNT = 1;
        }

        final AlbumFragment albumFragment = new AlbumFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_gallery_container, albumFragment).commit();

        galleryManager = new GalleryManager(this, new GalleryManager.GalleryListener() {
            @Override
            public void onGetAlbums(List<Album> albumList) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_gallery_container);
                if (currentFragment instanceof AlbumFragment) {
                    AlbumFragment fragment = (AlbumFragment) currentFragment;
                    fragment.setAlbumList(albumList);
                }
            }

            @Override
            public void onGetPhotos(List<Photo> photoList) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_gallery_container);
                if (currentFragment instanceof PhotoFragment) {
                    PhotoFragment fragment = (PhotoFragment) currentFragment;
                    fragment.setPhotoList(photoList);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_select) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_gallery_container);
            if (currentFragment instanceof PhotoFragment) {
                PhotoFragment photoFragment = (PhotoFragment) currentFragment;
                List<Photo> photoList = photoFragment.getSelectedPhotos();

                ArrayList<Photo> photoArrayList = new ArrayList<>(photoList);
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(MainActivity.KEY_SELECTED_PHOTOS, photoArrayList);
                setResult(RESULT_OK, intent);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof AlbumFragment) {
            AlbumFragment albumFragment = (AlbumFragment) fragment;
            albumFragment.setGalleryInteractionListener(this);
        } else if (fragment instanceof PhotoFragment) {
            PhotoFragment photoFragment = (PhotoFragment) fragment;
            photoFragment.setGalleryInteractionListener(this);
        } else {
            //Nothing
        }
    }

    @Override
    public void onReadAlbumList() {
        Log.d(TAG, "onReadAlbumList");
        if (type == GalleryType.IMAGE) {
            galleryManager.getImageAlbums();
        } else {
            galleryManager.getVideoAlbums();
        }
    }

    @Override
    public void onReadPhotoList(Album album) {
        Log.d(TAG, "onReadPhotoList");
        if (type == GalleryType.IMAGE) {
            galleryManager.getImagePhotos(album);
        } else {
            galleryManager.getVideoPhotos(album);
        }
    }

    @Override
    public void onSelectedAlbum(Album album) {
        Log.d(TAG, "onSelectedAlbum: " + album);

        Bundle bundle = new Bundle();
        bundle.putParcelable(PhotoFragment.KEY_ALBUM, album);

        final PhotoFragment photoFragment = new PhotoFragment();
        photoFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment_gallery_container, photoFragment).commit();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).onTrimMemory(level);
    }
}
