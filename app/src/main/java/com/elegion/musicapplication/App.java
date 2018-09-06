package com.elegion.musicapplication;


import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.elegion.musicapplication.db.DataBase;

public class App extends Application {

    private static SharedPreferencesHelper mSharedPreferencesHelper;
    private DataBase mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabase = Room.databaseBuilder(getApplicationContext(), DataBase.class, "music_database")
                .fallbackToDestructiveMigration()
                .build();

        mSharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
    }

    public DataBase getDatabase() {
        return mDatabase;
    }

    public static SharedPreferencesHelper getSharedPreferencesHelper() {
        return mSharedPreferencesHelper;
    }
}
