package com.crossapps.petpal.Server.Response;

import java.util.ArrayList;

public class PetResponse {
    private ArrayList<PetResponseData> data;
    private String message;
    private boolean success;

    public String getMessage() {
        return message;
    }

    public ArrayList<PetResponseData> getData() {
        return data;
    }

    public boolean getSuccess(){
        return success;
    }
}
