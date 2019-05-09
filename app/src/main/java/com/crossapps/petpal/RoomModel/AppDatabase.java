package com.crossapps.petpal.RoomModel;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.crossapps.petpal.RoomModel.DataAccessObjects.PetsModelDao;
import com.crossapps.petpal.RoomModel.Entities.PetsModel;

@Database(entities = {PetsModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "pets_db").build();
        }
        return INSTANCE;
    }

    public abstract PetsModelDao petsModel();

}
