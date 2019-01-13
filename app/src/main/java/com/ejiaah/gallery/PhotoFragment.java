package com.ejiaah.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class PhotoFragment extends Fragment {

    private static final String TAG = PhotoFragment.class.getSimpleName();

    public static final String KEY_ALBUM = "KeyAlbum";

    private RecyclerView photoRecyclerView;
    private PhotoRecyclerAdapter photoRecyclerAdapter;
    private MenuItem menuItem;

    private GalleryInteractionListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        photoRecyclerView = (RecyclerView) view.findViewById(R.id.photo_recyclerView);
        photoRecyclerAdapter = new PhotoRecyclerAdapter(getContext(), new PhotoRecyclerAdapter.SelectPhotoListener() {
            @Override
            public void onSelectedPhotoCount(int count) {
                if (menuItem == null) {
                    return;
                }
                if (count > 0) {
                    menuItem.setTitle(getString(R.string.select) + " (" + String.valueOf(count) + ")");
                } else {
                    menuItem.setTitle(getString(R.string.select));
                }
            }
        });
        photoRecyclerView.setAdapter(photoRecyclerAdapter);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        photoRecyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getArguments();
        Album album = bundle.getParcelable(KEY_ALBUM);

        listener.onReadPhotoList(album);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuItem = menu.findItem(R.id.action_select);
        menuItem.setVisible(true);
    }

    public void setGalleryInteractionListener(GalleryInteractionListener listener) {
        this.listener = listener;
    }

    public void setPhotoList(List<Photo> photoList) {
        photoRecyclerAdapter.setPhotoList(photoList);
    }

    public List<Photo> getSelectedPhotos() {
        return photoRecyclerAdapter.getSelectedPhotos();
    }

}
