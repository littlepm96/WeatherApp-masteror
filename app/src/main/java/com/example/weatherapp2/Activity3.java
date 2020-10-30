package com.example.weatherapp2;

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

    class Weather extends AsyncTask<String,Void,String> {  //First String means URL is in String, Void mean nothing, Third String means Return type will be String

        @Override
        protected String doInBackground(String... address) {
            //String... means multiple address can be send. It acts as array
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //Establish connection with address
                connection.connect();

                //retrieve data from url
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                //Retrieve data and return it as String
                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        ArrayList<Meteo> city = new ArrayList<>();

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView vi = findViewById(R.id.city);
        vi.setText(message);



        String cName = message.toString();



        String content;
        Weather weather = new Weather();
        try {



            content = weather.execute("http://api.openweathermap.org/data/2.5/forecast?q=" +
                    cName+"&APPID=fc87ff947ff79d8e26cc89dc744d00bc&lang=it").get();

            //First we will check data is retrieve successfully or not

            Log.d("contentData",content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            JSONArray lista = jsonObject.getJSONArray("list");

            //String mainTemperature = jsonObject.getString("main"); //this main is not part of weather array, it's seperate variable like weather
            //double visibility;
            //JSONArray array = new JSONArray(weatherData);

            String data = "";
            String description = "";
            String temperature = "";

            for(int i=0; i<lista.length(); i++){
                JSONObject weatherPart = lista.getJSONObject(i);
                data= weatherPart.getString("dt_txt");
                JSONArray bo = weatherPart.getJSONArray("weather");
                JSONObject maint = weatherPart.getJSONObject("main");


                temperature=maint.getString("temp");

                JSONObject desc=bo.getJSONObject(0);
                description=desc.getString("description");

                /////////////////////////////////////////////////////////

                double t = Double.valueOf(temperature) - 273.15;
                String s=Double.toString(Double.parseDouble(new DecimalFormat("##.##").format(t)));
                //t =Double.parseDouble(new DecimalFormat("##.##").format(t));
                //String s=Double.toString(t);
                s=s+" C°";

                Meteo j =new Meteo(data,description,s);

                city.add(j);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView =findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter_meteo(city));//try work
    };
    public ArrayList<Meteo> work(String cName){

        String url ="http://api.openweathermap.org/data/2.5/forecast?q=" +
                cName+"&APPID=fc87ff947ff79d8e26cc89dc744d00bc&lang=it";
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final ArrayList<Meteo> meteos = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray lista = jsonObject.getJSONArray("list");
                            String weatherData = jsonObject.getString("weather");
                            String mainTemperature = jsonObject.getString("main");
                            String data = "";
                            String description = "";
                            String temperature = "";

                            for(int i=0; i<lista.length(); i++){
                                JSONObject weatherPart = lista.getJSONObject(i);
                                data= weatherPart.getString("dt_txt");
                                JSONArray bo = weatherPart.getJSONArray("weather");
                                JSONObject maint = weatherPart.getJSONObject("main");


                                temperature=maint.getString("temp");

                                JSONObject desc=bo.getJSONObject(0);
                                description=desc.getString("description");

                                /////////////////////////////////////////////////////////

                                double t = Double.valueOf(temperature) - 273.15;
                                String s=Double.toString(Double.parseDouble(new DecimalFormat("##.##").format(t)));
                                //t =Double.parseDouble(new DecimalFormat("##.##").format(t));
                                //String s=Double.toString(t);
                                s=s+" C°";

                                Meteo j =new Meteo(data,description,s);

                             meteos.add(j);

                                new Thread(new Runnable() {
                            @Override
                            public void run() {
                              GestioneDB db = new GestioneDB(getApplicationContext());

                                //Apertura DB e visualizzazione del contenuto

                                db.open();
                                for(Meteo meteo :meteos){
                                    meteoTable.insert(db.db, meteo);
                                    db.close();
                                }
                            }
                        }).start();

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView vi=findViewById(R.id.city);
                vi.setText("That didn't work!");
                //queue.cancelAll(this);
            }
        });
        //queue.add(stringRequest);
        MyVolley.getInstance(this).getQueue().add(stringRequest);

GestioneDB db=new GestioneDB(this);
db.open();
ArrayList<Meteo> arrayList;
arrayList=meteoTable.selectAll(db.db);
        return arrayList;
    }
}




