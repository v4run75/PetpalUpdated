package com.crossapps.petpal.Server.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class MediaModel implements Parcelable {
    private String path;
    private String type;
    private Uri uri;

    public MediaModel(String path, String type, Uri uri) {
        this.path = path;
        this.type = type;
        this.uri = uri;
    }


    protected MediaModel(Parcel in) {
        path = in.readString();
        type = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<MediaModel> CREATOR = new Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel in) {
            return new MediaModel(in);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public Uri getUri() {
        return uri;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(type);
        dest.writeParcelable(uri, flags);
    }
}
