package com.example.weatherapp2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Activity2 extends AppCompatActivity {
    public  String  loadFromRaw(){

        InputStream is = getResources().openRawResource(R.raw.city);

        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }} catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonString = writer.toString();



        return  jsonString;
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.ez);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        ArrayList<String> city=new ArrayList<>();

      /*  try {
            JSONArray array = new JSONArray(loadJSONFromAsset());

            for (int i=0;i< array.length();i++){
            JSONObject cit = array.getJSONObject(i);
            if(!(cit.get("country").toString().equals("IT"))) {}
            else city.add((String) cit.get("name")); }


        } catch (JSONException e) {
            e.printStackTrace();
        }
*/

        //INIZIO DATABASE

        GestioneDB db = new GestioneDB(this);

        //Apertura DB ed aggiunta
        db.open();
        long id = db.inserisciPreferito("ferro vecchio", "Via truzzo");
        db.close();

        //Eliminare dal DB
        //SE NECESSARIO
/*
        db.open();
        boolean x = db.cancellaPreferito(0);
        boolean y = db.cancellaPreferito(1);
        boolean z = db.cancellaPreferito(2);
        db.close();

 */

        //Apertura DB e visualizzazione del contenuto

        db.open();
        Cursor c = db.ottieniTuttiPreferiti();
        if (c.moveToFirst()) {
            do {
                city.add(c.getString(1));
                Toast.makeText(this, "id: " + c.getString(0) + "\n" +
                                "Nome: " + c.getString(1) + "\n" +
                                "Indirizzo: " + c.getString(2),
                        Toast.LENGTH_LONG).show();
            } while (c.moveToNext());
        }
        db.close();

        Collections.sort(city, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        //FINE DB

        RecyclerView recyclerView =findViewById(R.id.view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new adapter(city));
    };

}