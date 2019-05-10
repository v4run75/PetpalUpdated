package com.crossapps.petpal.Server.Response;

import android.os.Parcel;
import android.os.Parcelable;

public class PetResponseData implements Parcelable {
    private String id;
    private String name;
    private String owner;
    private String contact;
    private String address;
    private String image;

    public PetResponseData(String id, String name, String owner, String contact, String address, String image) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.contact = contact;
        this.address = address;
        this.image = image;
    }

    private PetResponseData(Parcel in) {
        id = in.readString();
        name = in.readString();
        owner = in.readString();
        contact = in.readString();
        address = in.readString();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(owner);
        dest.writeString(contact);
        dest.writeString(address);
        dest.writeString(image);
    }

    public static final Parcelable.Creator<PetResponseData> CREATOR
            = new Parcelable.Creator<PetResponseData>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public PetResponseData createFromParcel(Parcel in) {
            return new PetResponseData(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public PetResponseData[] newArray(int size) {
            return new PetResponseData[size];
        }
    };


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

}
