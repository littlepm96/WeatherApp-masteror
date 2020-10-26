package com.example.weatherapp2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.weatherapp2.MainActivity.hideKeyboard;

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
        ArrayList<meteo> city = new ArrayList<>();

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView vi = findViewById(R.id.city);
        vi.setText(message);

        //View cityName = findViewById(R.id.Ab);
        //View searchButton = findViewById(R.id.searchButton);
        //View result = findViewById(R.id.resut);

        String cName = message.toString();

        Log.d("qua", "search:"+cName);

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
                s=s+" C*";

                meteo j =new meteo(data,description,s);

                city.add(j);

            }

            //JSONObject mainPart = new JSONObject(mainTemperature);
            //temperature = mainPart.getString("temp");

            //visibility = Double.parseDouble(jsonObject.getString("visibility"));
            //By default visibility is in meter
            //int visibilityInKilometer = (int) visibility/1000;

            //Log.i("Temperature",temperature);

            /*Log.i("main",main);
            Log.i("description",description);*/

            //double t = Double.valueOf(temperature) - 273.15;
            //t =Double.parseDouble(new DecimalFormat("##.##").format(t));


            /*String resultText = "Main :                     "+main+
                    "\nDescription :        "+description +
                    "\nTemperature :        "+t +"*C"+
                    "\nVisibility :              "+visibilityInKilometer+" KM";
            hideKeyboard(this);
            //result.setText(resultText);*/

            //Now we will show this result on screen

        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView =findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter_meteo(city));
    };
}
