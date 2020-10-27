package com.example.weatherapp2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
    private static final String EXTRA_MESSAGE ="com.example.weatherapp2" ;

    public  String  loadFromRaw(){

        InputStream is = getResources().openRawResource(R.raw.ez);

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

                      // Creazione DATABASE

        GestioneDB db = new GestioneDB(this);

                //Apertura DB e visualizzazione del contenuto

        db.open();
        Cursor c = db.ottieniTuttiPreferiti();
        if (c.moveToFirst()) {
            do {
                city.add(c.getString(1));
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

                //ELIMINA PREFERITO

    public void delpreferiti(View view) {


        TextView textView = (TextView) findViewById(R.id.title);
        String message = textView.getText().toString();
        String city = message;
        GestioneDB db= new GestioneDB(getApplicationContext());

        if (!(city.equals(""))){db.cancellaPreferito(city);}



    }

    public void setpreferiti(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        TextView Text =  findViewById(R.id.title);
        String message = Text.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);

    }
}

