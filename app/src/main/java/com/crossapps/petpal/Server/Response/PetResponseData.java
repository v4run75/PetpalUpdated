package com.crossapps.petpal.Server.Response;

public class PetResponseData {
    private String id;
    private String name;
    private String image;

    public PetResponseData(String id,String name,String image){
        this.id=id;
        this.name=name;
        this.image=image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
