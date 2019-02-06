package com.ejiaah.gallery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AlbumFragment extends Fragment {

    private static final String TAG = AlbumFragment.class.getSimpleName();

    private GalleryInteractionListener listener;

    private RecyclerView recyclerView;
    private AlbumRecyclerAdapter albumRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.album_recyclerView);
        albumRecyclerAdapter = new AlbumRecyclerAdapter(getActivity(), listener);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(albumRecyclerAdapter);

        listener.onReadAlbumList();

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_select);
        menuItem.setVisible(false);
    }

    public void setGalleryInteractionListener(GalleryInteractionListener listener) {
        this.listener = listener;
    }

    public void setAlbumList(@NonNull List<Album> albumList) {
        albumRecyclerAdapter.setAlbumList(albumList);
    }

}
