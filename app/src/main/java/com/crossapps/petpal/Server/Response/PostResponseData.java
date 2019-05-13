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
    /*{
        "link": "http://webpulse.co/pathprahari/uploaded_files/user_post/MP4_20190316_102831_406465368354707724v5nh.mp4",
        "type": "video"
    }*/
//    ]
//},

/*

{"message":"Login Success",
        "data":
        {"postId":"1"
        ,"userId":"8",
        "description":"Corgi 4 months old",
        "created":"2019-05-13",
        "medias":"{\"link\":\"https:\/\/images.dog.ceo\/breeds\/corgi-cardigan\/n02113186_11559.jpg\"," +
        "\"type\":\"image\"}"
        ,"user":
        {"userId":"8",
        "email":"varun.webpulse@gmail.com",
        "name":"Varun",
        "profilePicture":""}},
        "success":true}
*/

public class PostResponseData implements Parcelable {
    private String postId;
    private String userId;
    private String description;
    private String created;
    private UserPost user;
    private ArrayList<Media> medias;


    private PostResponseData(Parcel in) {
        postId = in.readString();
        userId = in.readString();
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

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
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
        dest.writeString(postId);
        dest.writeString(userId);
        dest.writeString(description);
        dest.writeString(created);
        dest.writeParcelable(user, flags);
        dest.writeTypedList(medias);
    }
}

