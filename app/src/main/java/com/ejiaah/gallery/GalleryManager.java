package com.ejiaah.gallery;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GalleryManager {

    private static final String TAG = GalleryManager.class.getCanonicalName();

    Context context;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainThread = new Handler(Looper.getMainLooper());

    private GalleryListener listener;

    public interface GalleryListener {
        void onGetAlbums(List<Album> albumList);

        void onGetPhotos(List<Photo> photoList);
    }

    GalleryManager(Context context, GalleryListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void getImageAlbums() {
        Log.d(TAG, "getImageAlbums");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Album> albumList = new ArrayList<>();
                if (context == null) {
                    mainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onGetAlbums(albumList);
                        }
                    });
                    return;
                }
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        "count(*)",
                        MediaStore.Images.Media.DATA
                };

                String selection = "1) GROUP BY (1";
                String orderBy = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC";

                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, projection, selection, null, orderBy);
                    int allPhotoCount = 0;
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count(*)"));
                            allPhotoCount += count;
                            Album album = new Album();
                            album.setBucketID(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)));
                            album.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                            album.setCount(count);
                            album.setPreview(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                            albumList.add(album);
                        }
                        Album album = new Album();
                        album.setBucketID("0");
                        album.setName(context.getString(R.string.all_album));
                        album.setCount(allPhotoCount);
                        album.setPreview("");
                        albumList.add(0, album);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onGetAlbums(albumList);
                    }
                });
            }
        };
        executor.execute(runnable);
    }

    public void getImagePhotos(final Album album) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Photo> photoList = new ArrayList<>();
                if (context == null || album == null) {
                    mainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onGetPhotos(photoList);
                        }
                    });
                    return;
                }
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATA
                };

                String selection = MediaStore.Images.Media.BUCKET_ID + " = " + album.getBucketID();
                String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";

                Cursor cursor = null;

                try {
                    if (album.getBucketID().equals("0")) {
                        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy);
                    } else {
                        cursor = context.getContentResolver().query(uri, projection, selection, null, orderBy);
                    }

                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            Photo photo = new Photo();
                            photo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
                            photo.setView(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                            photoList.add(photo);
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onGetPhotos(photoList);
                    }
                });
            }
        };
        executor.execute(runnable);
    }

    public void getVideoAlbums() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Album> albumList = new ArrayList<>();
                if (context == null) {
                    mainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onGetAlbums(albumList);
                        }
                    });
                    return;
                }
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {
                        MediaStore.Video.Media.BUCKET_ID,
                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                        "count(*)",
                        MediaStore.Video.Media.DATA
                };

                String selection = "1) GROUP BY (1";
                String orderBy = MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " ASC";

                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, projection, selection, null, orderBy);
                    int allPhotoCount = 0;
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            int count = cursor.getInt(cursor.getColumnIndexOrThrow("count(*)"));
                            allPhotoCount += count;
                            Album album = new Album();
                            album.setBucketID(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)));
                            album.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)));
                            album.setCount(count);
                            album.setPreview(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                            albumList.add(album);
                        }
                        Album album = new Album();
                        album.setBucketID("0");
                        album.setName(context.getString(R.string.all_album));
                        album.setCount(allPhotoCount);
                        album.setPreview("");
                        albumList.add(0, album);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onGetAlbums(albumList);
                    }
                });
            }
        };
        executor.execute(runnable);
    }

    public void getVideoPhotos(final Album album) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Photo> photoList = new ArrayList<>();
                if (context == null || album == null) {
                    mainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onGetPhotos(photoList);
                        }
                    });
                    return;
                }
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.DATA
                };

                String selection = MediaStore.Video.Media.BUCKET_ID + " = " + album.getBucketID();
                String orderBy = MediaStore.Video.Media.DATE_TAKEN + " DESC";

                Cursor cursor = null;
                try {
                    if (album.getBucketID().equals("0")) {
                        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy);
                    } else {
                        cursor = context.getContentResolver().query(uri, projection, selection, null, orderBy);
                    }

                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            Photo photo = new Photo();
                            photo.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)));
                            photo.setView(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                            photoList.add(photo);
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onGetPhotos(photoList);
                    }
                });
            }
        };
        executor.execute(runnable);
    }
}
