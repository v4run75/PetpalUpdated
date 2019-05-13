package com.crossapps.petpal.Server.Response;

import java.util.ArrayList;

public class PostResponse {
    private ArrayList<PostResponseData> data;
    private boolean success;
    private String message;


    public ArrayList<PostResponseData> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean getSuccess(){
        return success;
    }
}

