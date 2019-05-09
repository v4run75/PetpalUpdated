package com.crossapps.petpal.RoomModel.DataAccessObjects;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.crossapps.petpal.RoomModel.Entities.PetsModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PetsModelDao {

    @Query("select * from pets")
    LiveData<List<PetsModel>> getAllPets();

    @Query("select * from pets where id = :id")
    PetsModel getPetbyId(String id);

    @Insert(onConflict = REPLACE)
    long addPet(PetsModel petsModel);

    @Delete
    void deletePet(PetsModel petsModel);

}