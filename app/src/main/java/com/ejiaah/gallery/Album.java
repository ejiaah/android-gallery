package com.ejiaah.gallery;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {

    private String bucketID = "";
    private String name = "";
    private int count = 0;
    private String preview = "";

    public Album() {
    }

    protected Album(Parcel in) {
        bucketID = in.readString();
        name = in.readString();
        count = in.readInt();
        preview = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public String getBucketID() {
        return bucketID;
    }

    public void setBucketID(String bucketID) {
        this.bucketID = bucketID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    @Override
    public String toString() {
        return "Album{" +
                "bucketID='" + bucketID + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", preview='" + preview + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bucketID);
        dest.writeString(name);
        dest.writeInt(count);
        dest.writeString(preview);
    }
}
