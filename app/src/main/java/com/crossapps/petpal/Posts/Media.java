package com.crossapps.petpal.Posts;

import android.os.Parcel;
import android.os.Parcelable;

public class Media implements Parcelable {
    private String link;
    private String type;

    public Media(String link, String type) {
        this.link = link;
        this.type = type;
    }

    protected Media(Parcel in) {
        link = in.readString();
        type = in.readString();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public String getType() {
        return type;
    }

    public String getLink() {
        return link;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLink(String link) {
        this.link = link;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(link);
        dest.writeString(type);
    }
}
