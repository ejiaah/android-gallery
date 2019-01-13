package com.ejiaah.gallery;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {

    private long id = 0;
    private String view = "";
    private boolean isSelected = false;

    public Photo() {
    }

    protected Photo(Parcel in) {
        id = in.readLong();
        view = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", view='" + view + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(view);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
