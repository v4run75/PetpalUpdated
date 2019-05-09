package com.crossapps.petpal.RoomModel.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import com.crossapps.petpal.RoomModel.AppDatabase;
import com.crossapps.petpal.RoomModel.Entities.PetsModel;

import java.util.List;

public class PetsViewModel extends AndroidViewModel {

    private final LiveData<List<PetsModel>> petsList;

    private AppDatabase appDatabase;

    public PetsViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

        petsList = appDatabase.petsModel().getAllPets();
    }


    public LiveData<List<PetsModel>> getPetsList() {
        return petsList;
    }

    public void deleteItem(PetsModel borrowModel) {
        new deleteAsyncTask(appDatabase).execute(borrowModel);
    }

    private static class deleteAsyncTask extends AsyncTask<PetsModel, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final PetsModel... params) {
            db.petsModel().deletePet(params[0]);
            return null;
        }


    }



    public void addItem(PetsModel borrowModel) {
        new addAsyncTask(appDatabase).execute(borrowModel);
    }



    private static class addAsyncTask extends AsyncTask<PetsModel, Void, Void> {

        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final PetsModel... params) {
            db.petsModel().addPet(params[0]);
            return null;
        }

    }
}