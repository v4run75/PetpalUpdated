package com.crossapps.petpal.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;
import com.crossapps.petpal.Posts.Media;
import com.crossapps.petpal.Server.Model.UserPost;

import java.util.ArrayList;

//{
//    "id": "25",
//    "total_shares": "0",
//    "total_comment": "0",
//    "total_like": "0",
//    "category": "HomePosts",
//    "description": "",
//    "created": "2019-03-16 06:59:17",
//    "user": {
//    "id": "30",
//    "email": "varunkumar_95@yahoo.com",
//    "first_name": "Varun Singh",
//    "last_name": null
//    "profile_picture":"null"
//},
//    "medias": [
//    {
//        "link": "http://webpulse.co/pathprahari/uploaded_files/user_post/MP4_20190316_102831_406465368354707724v5nh.mp4",
//        "type": "video"
//    }
//    ]
//},

public class PostResponseData implements Parcelable {
    private String id;
    private String description;
    private String created;
    private UserPost user;
    private ArrayList<Media> medias;


    protected PostResponseData(Parcel in) {
        id = in.readString();
        description = in.readString();
        created = in.readString();
        user = in.readParcelable(UserPost.class.getClassLoader());
        medias = in.createTypedArrayList(Media.CREATOR);
    }

    public static final Creator<PostResponseData> CREATOR = new Creator<PostResponseData>() {
        @Override
        public PostResponseData createFromParcel(Parcel in) {
            return new PostResponseData(in);
        }

        @Override
        public PostResponseData[] newArray(int size) {
            return new PostResponseData[size];
        }
    };


    public String getId() {
        return id;
    }

    public ArrayList<Media> getMedias() {
        return medias;
    }

    public UserPost getUser() {
        return user;
    }


    public String getCreated() {
        return created;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(created);
        dest.writeParcelable(user, flags);
        dest.writeTypedList(medias);
    }
}

