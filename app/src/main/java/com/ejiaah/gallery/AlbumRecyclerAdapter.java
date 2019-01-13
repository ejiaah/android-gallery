package com.ejiaah.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class AlbumRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerAdapter.ViewHolder> {

    private List<Album> albumList = new ArrayList<>();
    private Context context;

    private GalleryInteractionListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView previewImageView;
        private TextView titleTextView;
        private TextView countTextView;

        public ViewHolder(View v) {
            super(v);
            previewImageView = (ImageView) v.findViewById(R.id.preview_imageView);
            titleTextView = (TextView) v.findViewById(R.id.title_textView);
            countTextView = (TextView) v.findViewById(R.id.count_textView);
        }
    }

    public AlbumRecyclerAdapter(Context context, GalleryInteractionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_album, parent, false);
        final ViewHolder viewHolder = new AlbumRecyclerAdapter.ViewHolder(view);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                listener.onSelectedAlbum(albumList.get(position));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumList.get(position);
        if (context == null || album == null) {
            return;
        }

        Glide.with(context)
                .load(album.getPreview())
                .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                .into(holder.previewImageView);

        holder.titleTextView.setText(album.getName());
        holder.countTextView.setText(String.valueOf(album.getCount()));
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void setAlbumList(@NonNull List<Album> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }
}