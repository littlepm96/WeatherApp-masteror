package com.example.weatherapp2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Forecast extends AppCompatActivity {
    RecyclerView recyclerView;
    Context context = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast);
        ArrayList<Meteo> city = new ArrayList<>();
        SQLiteDatabase db = MyDatabase.getInstance(getApplicationContext()).getWritableDatabase();
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView vi = findViewById(R.id.city);
        String cName = message;
        if (message.contains("q=")) message = message.substring(2);
        if (message.contains("lat=")) message = "la tua posizione";
        vi.setText(message);
        recyclerView = findViewById(R.id.lista);


        Log.d("AAAAAAAAAAAAAAAA", cName);
        //try work


        String url = "http://api.openweathermap.org/data/2.5/forecast?" +
                cName + "&APPID=fc87ff947ff79d8e26cc89dc744d00bc&lang=it";
        ///http://api.openweathermap.org/data/2.5/forecast?lat=35&lon=139&appid=fc87ff947ff79d8e26cc89dc744d00bc
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //run on ui thread
                            }
                        }).start();

                        final ArrayList<Meteo> meteos = new ArrayList<>();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray lista = jsonObject.getJSONArray("list");

                            String data;
                            String description;
                            String temperature = "";

                            for (int i = 0; i < lista.length(); i++) {
                                JSONObject weatherPart = lista.getJSONObject(i);
                                data = weatherPart.getString("dt_txt");
                                JSONArray bo = weatherPart.getJSONArray("weather");
                                JSONObject maint = weatherPart.getJSONObject("main");
                                temperature = maint.getString("temp");
                                JSONObject desc = bo.getJSONObject(0);
                                description = desc.getString("description");

                                /////////////////////////////////////////////////////////

                                double t = Double.parseDouble(temperature) - 273.15;
                                String s = Double.toString(Double.parseDouble(new DecimalFormat("##.##").format(t)));
                                //t =Double.parseDouble(new DecimalFormat("##.##").format(t));
                                //String s=Double.toString(t);
                                s = s + " C°";

                                Meteo j = new Meteo(data, description, s);

                                meteos.add(j);
                            }

                            SQLiteDatabase db = MyDatabase.getInstance(getApplicationContext()).getWritableDatabase();

                            //Apertura DB e visualizzazione del contenuto

                            for (Meteo meteo : meteos) {
                                MeteoTable.insert(db, meteo);

                            }
                            ArrayList<Meteo> arrayList;

                            arrayList = MeteoTable.selectAll(db);

                            recyclerView.setAdapter(new AdapterMeteo(arrayList));
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));


                            MeteoTable.deleteAll(db);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {

                TextView vi = findViewById(R.id.city);
                vi.setText("That didn't work! prova ad inserire il nome di una città valido");

            }
        });

        MyVolley.getInstance(this).getQueue().add(stringRequest);


    }
}




