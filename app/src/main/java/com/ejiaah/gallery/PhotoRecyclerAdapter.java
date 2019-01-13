package com.ejiaah.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder> {

    private final String TAG = PhotoRecyclerAdapter.class.getSimpleName();

    private Context context;
    private List<Photo> photoList = new ArrayList<>();
    private Set<Integer> selectedPositionSet = new HashSet<>();

    private SelectPhotoListener listener;

    public interface SelectPhotoListener {
        void onSelectedPhotoCount(int count);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView previewImageView;
        CheckBox selectCheckBox;

        public ViewHolder(View v) {
            super(v);
            previewImageView = (ImageView) v.findViewById(R.id.preview_imageView);
            selectCheckBox = (CheckBox) v.findViewById(R.id.select_checkBox);
        }
    }

    public PhotoRecyclerAdapter(Context context, SelectPhotoListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhotoRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_photo, parent, false);
        final ViewHolder viewHolder = new PhotoRecyclerAdapter.ViewHolder(view);

        viewHolder.selectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                Photo photo = photoList.get(position);

                if (viewHolder.selectCheckBox.isChecked()) {
                    if (selectedPositionSet.size() > GalleryActivity.LIMIT_SELECT_COUNT - 1) {
                        viewHolder.selectCheckBox.setChecked(false);
                        Toast.makeText(context, "LIMIT_SELECT_COUNT", Toast.LENGTH_SHORT).show();
                    } else {
                        photo.setSelected(true);
                        selectedPositionSet.add(position);
                    }
                } else {
                    photo.setSelected(false);
                    if (selectedPositionSet.contains(position)) {
                        selectedPositionSet.remove(position);
                    }
                }
                listener.onSelectedPhotoCount(selectedPositionSet.size());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoRecyclerAdapter.ViewHolder holder, final int position) {
        Photo photo = photoList.get(position);

        if (context == null || photo == null) {
            return;
        }

        Glide.with(context)
                .load(photo.getView())
                .into(holder.previewImageView);

        holder.selectCheckBox.setChecked(photo.isSelected());
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void setPhotoList(@NonNull List<Photo> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    public List<Photo> getSelectedPhotos() {
        List<Photo> photoList = new ArrayList<>();
        for (int index : selectedPositionSet) {
            photoList.add(this.photoList.get(index));
        }
        return photoList;
    }

}