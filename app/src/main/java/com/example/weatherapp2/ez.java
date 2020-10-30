package com.example.weatherapp2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;



public class ez extends AppCompatActivity{
    ArrayList<String> nomi;

    public ez(ArrayList<String> nomi) {
        this.nomi = nomi;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.ez);  //option- in city ci sono 43MB di città di tutto il mondo
            int size = is.available();                                  //filtrate per Paese(it). il file ez.json contiene solo quelle italiane
            byte[] buffer = new byte[size];                             //più qualche errore ma è molto meno ingombrante(in caso si ricrea meglio)
            is.read(buffer);                                            //al momento è in uso City. se si cambia in ez è molto più veloce
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    public ArrayList<String> arrayListLoad() {
        ArrayList<String> z=new ArrayList<String>();
        try {

            JSONArray array = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < array.length(); i++) {
                JSONObject cit = array.getJSONObject(i);
                if (!(cit.get("country").toString().equals("IT"))) {
                } else z.add((String) cit.get("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return z;

    }


}
