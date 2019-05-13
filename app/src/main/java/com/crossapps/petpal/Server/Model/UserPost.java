package com.crossapps.petpal.Server.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserPost implements Parcelable {
    private String userId;
    private String email;
    private String name;
    private String profilePicture;


    private UserPost(Parcel in) {
        userId = in.readString();
        email = in.readString();
        name = in.readString();
        profilePicture = in.readString();
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

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(profilePicture);
    }
}
//"user": {
//    "id": "30",
//    "email": "varunkumar_95@yahoo.com",
//    "first_name": "Varun Singh",
//    "last_name": null
//},