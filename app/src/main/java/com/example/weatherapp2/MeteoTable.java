package com.example.weatherapp2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class meteoTable {

    public static final String TABLE = "meteo";
    public static final String ID = "id";
    public static final String DATA = "data";
    public static final String METEO = "meteo";
    public static final String TEMP = "temperature";


    public static void create(SQLiteDatabase database) {
        String sql = "CREATE TABLE " + TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATA + " TEXT, " +
                METEO + " TEXT, " +
                TEMP + " TEXT" +
                ")";
        database.execSQL(sql);
    }

    public static void insert(SQLiteDatabase database, Meteo meteo){

        ContentValues values = new ContentValues();
        values.put(DATA, meteo.getData());
        values.put(METEO, meteo.getMeteo());
        values.put(TEMP, meteo.getTemp());


        long res = database.insert(TABLE, null, values);
        meteo.setId(res);
    }

    public static boolean update(SQLiteDatabase database, Meteo meteo){
        ContentValues values = new ContentValues();
        values.put(DATA, meteo.getData());
        values.put(METEO, meteo.getMeteo());
        values.put(TEMP, meteo.getTemp());

        return database.update(TABLE, values, "ID = " + meteo.getId(), null) == 1;
        // database.update(TABLE, values, "ID = ?", new String[] { String.valueOf(city.getId()) });
    }

    public static boolean deleteAll(SQLiteDatabase database) {

        return database.delete(TABLE, null, null) > 0;
    }

    public static ArrayList<Meteo> selectAll(SQLiteDatabase database){

        ArrayList<Meteo> meteos = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE + " ORDER BY " + DATA + " ASC";

        Cursor cursor = database.rawQuery(sql, null);
        // database.query(TABLE, null, null, null, null, null, NAME);

        while(cursor.moveToNext()){
            Meteo meteo = new Meteo();

            meteo.setId(cursor.getLong(cursor.getColumnIndex(ID)));
            meteo.setData(cursor.getString(cursor.getColumnIndex(DATA)));
            meteo.setMeteo(cursor.getString(cursor.getColumnIndex(METEO)));
            meteo.setTemp(cursor.getString(cursor.getColumnIndex(TEMP)));


            meteos.add(meteo);
        }

        cursor.close();

        return meteos;
  }  }

