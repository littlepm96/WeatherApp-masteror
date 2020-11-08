package com.example.weatherapp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    private static MyDatabase instance = null;

    public static MyDatabase getInstance(Context context) {
        return instance == null ? instance = new MyDatabase(context) : instance;
    }

    public MyDatabase(@Nullable Context context) {
        super(context, "testDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        meteoTable.create(db);
        preferitiTable.create(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
