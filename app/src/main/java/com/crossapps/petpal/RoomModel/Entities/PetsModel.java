package com.crossapps.petpal.RoomModel.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "pets")
public class PetsModel {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "owner")
    private String owner;
    @ColumnInfo(name = "contact")
    private String contact;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "image")
    private String image;

    public PetsModel(String name, String owner, String contact, String address,String image) {
        this.name = name;
        this.owner = owner;
        this.contact = contact;
        this.address = address;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getOwner() {
        return owner;
    }

    public String getImage() {
        return image;
    }
}
