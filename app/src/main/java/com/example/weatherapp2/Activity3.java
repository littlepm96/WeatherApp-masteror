package com.example.weatherapp2;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Activity3 extends AppCompatActivity {

    Context context=this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        ArrayList<Meteo> city = new ArrayList<>();
        SQLiteDatabase db = MyDatabase.getInstance(getApplicationContext()).getWritableDatabase();
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView vi = findViewById(R.id.city);
        String cName = message.toString();
        if(message.contains("q="))message=message.substring(2);
        vi.setText(message);


        Log.d("AAAAAAAAAAAAAAAA", cName);
        //try work


        String url ="http://api.openweathermap.org/data/2.5/forecast?"+
                cName+"&APPID=fc87ff947ff79d8e26cc89dc744d00bc&lang=it";
       ///http://api.openweathermap.org/data/2.5/forecast?lat=35&lon=139&appid=fc87ff947ff79d8e26cc89dc744d00bc
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final ArrayList<Meteo> meteos = new ArrayList<>();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray lista = jsonObject.getJSONArray("list");

                            String data = "";
                            String description = "";
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

                                double t = Double.valueOf(temperature) - 273.15;
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
                                meteoTable.insert(db, meteo);


                           /* new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    SQLiteDatabase db = MyDatabase.getInstance(getApplicationContext()).getWritableDatabase();

                                    //Apertura DB e visualizzazione del contenuto
                                    for (Meteo meteo : meteos) {
                                        meteoTable.insert(db, meteo);
                                    }
                                }
                            }).start();*/



                        }          ArrayList<Meteo> arrayList;


                            RecyclerView recyclerView =findViewById(R.id.lista);

                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            arrayList=  meteoTable.selectAll(db);
                            meteoTable.deleteAll(db);
                            recyclerView.setAdapter(new Adapter_meteo(arrayList));}
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                TextView vi=findViewById(R.id.city);
                vi.setText("That didn't work! prova ad inserire il nome di una città valido");
                //queue.cancelAll(this);
            }
        });
        //queue.add(stringRequest);
        MyVolley.getInstance(this).getQueue().add(stringRequest);

       /* ArrayList<Meteo> arrayList;


        RecyclerView recyclerView =findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList=  meteoTable.selectAll(db);
        meteoTable.deleteAll(db);
        recyclerView.setAdapter(new Adapter_meteo(arrayList));*/

}}




