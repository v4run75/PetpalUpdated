package com.crossapps.petpal.Server.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserPost implements Parcelable {
    private String id;
    private String email;
    private String first_name;
    private String last_name;
    private String profile_picture;


    protected UserPost(Parcel in) {
        id = in.readString();
        email = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        profile_picture = in.readString();
    }

    public static final Creator<UserPost> CREATOR = new Creator<UserPost>() {
        @Override
        public UserPost createFromParcel(Parcel in) {
            return new UserPost(in);
        }

        @Override
        public UserPost[] newArray(int size) {
            return new UserPost[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(profile_picture);
    }
}
//"user": {
//    "id": "30",
//    "email": "varunkumar_95@yahoo.com",
//    "first_name": "Varun Singh",
//    "last_name": null
//},