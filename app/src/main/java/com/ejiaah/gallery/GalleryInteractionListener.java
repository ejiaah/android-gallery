package com.ejiaah.gallery;

public interface GalleryInteractionListener {
    void onReadAlbumList();

    void onReadPhotoList(Album album);

    void onSelectedAlbum(Album album);
}
