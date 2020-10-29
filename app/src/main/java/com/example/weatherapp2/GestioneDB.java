package com.example.weatherapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GestioneDB {

    static final String KEY_RIGAID = "id";
    static final String KEY_NOME = "nome";
    //static final String KEY_INDIRIZZO = "indirizzo";
    static final String TAG = "GestioneDB";
    static final String DATABASE_NOME = "TestDB";
    static final String DATABASE_TABELLA = "clienti";
    static final int DATABASE_VERSIONE = 1;

    static final String DATABASE_CREAZIONE =
            "CREATE TABLE clienti (id integer primary key autoincrement, "
                    + "nome text not null);";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

                            //CLASS

    public GestioneDB(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NOME, null, DATABASE_VERSIONE);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREAZIONE);
                String s = "CREATE TABLE meteo (id integer primary key autoincrement, data text not null, meteo text not null,temperatura text not null)";
                db.execSQL(s);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            Log.w(DatabaseHelper.class.getName(),"Aggiornamento database dalla versione " + oldVersion + " alla "
                    + newVersion + ". I dati esistenti verranno eliminati.");
            db.execSQL("DROP TABLE IF EXISTS clienti");
            onCreate(db);
        }

    }

public void drop (){
    db.execSQL("DROP TABLE IF EXISTS clienti");

   }


    public GestioneDB open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }


    public void close() {
        DBHelper.close();
    }

/*
    public long inserisciPreferito(String nome, String indirizzo) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOME, nome);
        initialValues.put(KEY_INDIRIZZO, indirizzo);
        return db.insert(DATABASE_TABELLA, null, initialValues);
    }
*/

    public long inserisciPreferito(String nome) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOME, nome);
        return db.insert(DATABASE_TABELLA, null, initialValues);
    }


    public boolean cancellaPreferito(String nome) {
        return db.delete(DATABASE_TABELLA, KEY_NOME + "=\"" + nome+"\"",null) > 0;
    }


    public Cursor ottieniTuttiPreferiti() {
        return db.query(DATABASE_TABELLA, new String[] {KEY_RIGAID, KEY_NOME}, null, null, null, null, null);
    }


    public Cursor ottieniPreferito(long rigaId) throws SQLException {
        Cursor mCursore = db.query(true, DATABASE_TABELLA, new String[] {KEY_RIGAID, KEY_NOME}, KEY_RIGAID + "=" + rigaId, null, null, null, null, null);
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        return mCursore;
    }
    public Cursor ottieniPreferito(String s) throws SQLException {
        Cursor mCursore = db.query(true, DATABASE_TABELLA, new String[] {KEY_RIGAID, KEY_NOME}, KEY_NOME + "=" + s, null, null, null, null, null);
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        return mCursore;
    }


    public boolean cancellaPreferito(long rigaId) {
        return db.delete(DATABASE_TABELLA, KEY_RIGAID + "=" + rigaId, null) > 0;
    }


    public boolean aggiornaPreferito (long rigaId, String name) {
        ContentValues args = new ContentValues();
        args.put(KEY_NOME, name);

        return db.update(DATABASE_TABELLA, args, KEY_RIGAID + "=" + rigaId, null) > 0;
    }


}
