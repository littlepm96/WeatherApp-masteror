package com.example.weatherapp2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class preferitiTable {

        public static final String TABLE = "preferiti";
        public static final String ID = "id";
        public static final String CITY = "city";

        public static void create(SQLiteDatabase database) {
            String sql = "CREATE TABLE preferiti (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CITY + " TEXT " +
                    ")";
            database.execSQL(sql);
        }

        public static void insert(SQLiteDatabase database, String pref){

            ContentValues values = new ContentValues();
            values.put(CITY,pref);


            database.insert(TABLE, null, values);

        }

        public static void update(SQLiteDatabase database, String pref){//non serve
            //ContentValues values = new ContentValues();
            //values.put(CITY, pref);
            database.execSQL("DROP TABLE IF EXISTS "+TABLE);
            create(database);
            //return database.update(TABLE, values, "city = " + pref, null) == 1;
            // database.update(TABLE, values, "ID = ?", new String[] { String.valueOf(city.getId()) });
        return ;}

        public static boolean deleteAll(SQLiteDatabase database) {

            return database.delete(TABLE, null, null) > 0;
        }

        public static ArrayList<String> selectAll(SQLiteDatabase database){

            ArrayList<String> pref = new ArrayList<>();

            String sql = "SELECT * FROM " + TABLE + " ORDER BY " + CITY + " ASC";

            Cursor cursor = database.rawQuery(sql, null);
            // database.query(TABLE, null, null, null, null, null, NAME);

            while(cursor.moveToNext()){

                String p = cursor.getString(cursor.getColumnIndex(CITY));

                pref.add(p);
            }

            cursor.close();

            return pref;
        }  }





