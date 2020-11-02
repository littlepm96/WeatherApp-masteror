package com.example.weatherapp2;

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

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOCATION_PEMISSION = 1;
    public static String EXTRA_MESSAGE = "com.example.weatherapp2";
    FusedLocationProviderClient fusedLocationClient;
    AutoCompleteTextView cityName;
    Button searchButton;
    TextView result;
    Context context = this;
    int requestcode = 3;
String w="";


              //Method for writing data to text file

    public String loadJSONFromAsset() {
        String json ;
        try {
            InputStream is = getResources().openRawResource(R.raw.ez);
            //option- in city ci sono 43MB di città di tutto il mondo
            //filtrate per Paese(it). il file ez.json contiene solo quelle italiane
            //più qualche errore ma è molto meno ingombrante(in caso si ricrea meglio)
            //al momento è in uso City. se si cambia in ez è molto più veloce
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


    public ArrayList<String> arrayListLoad(ArrayList z) {

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

                //MENÙ CONTESTUALE IN ALTO A DESTRA

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

                //SCELTA DELLE AZIONI DEI PULSANTI INTERNO AL MENÙ

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.exit:
                new exit_dialog().show(getSupportFragmentManager(), "exit");
                break;

            case R.id.preferiti:

                Intent intent = new Intent(this, Activity2.class);

                startActivityForResult(intent, requestcode);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra("key");
                EditText editText = (EditText) findViewById(R.id.Ab);
                editText.setText(str);
            }
        }
    }
                    //CHIAMATA ALLA TERZA VIEW

    public void next(View view) {

        String s = "meteo di 5 giorni";
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Activity3.class);

        EditText editText = (EditText) findViewById(R.id.Ab);

        String message = "q="+editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivityForResult(intent,2);
        hideKeyboard(this);
    }

    //lat=35&lon=139



                    //AGGIUNGI A PREFERITI

    public void addPreferiti(View view) {
        EditText editText = (EditText) findViewById(R.id.Ab);
        String message = editText.getText().toString();

        GestioneDB db = new GestioneDB(context);
        db.open();
        String city = message;
        if (!(city.equals(""))) {
            long id = db.inserisciPreferito(message);
            String s = "Aggiunto ai preferiti!";
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            db.close();
        }else{String s = "nulla da aggiungere ai preferiti!";
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();}
    }


                        //NASCONDE LA TASTIERA

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


                    //METODO PER LA RICERCA

    public void search(View view) {
        cityName = findViewById(R.id.Ab);
        searchButton = findViewById(R.id.searchButton);
        result = findViewById(R.id.resut);

        String cName = cityName.getText().toString();

        String url ="https://api.openweathermap.org/data/2.5/weather?q=" +
                cName + "&APPID=fc87ff947ff79d8e26cc89dc744d00bc&lang=it";
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                        String weatherData = jsonObject.getString("weather");
                        String mainTemperature = jsonObject.getString("main");
                        double visibility;

                        JSONArray array = new JSONArray(weatherData);

                        String main = "";
                        String description = "";
                        String temperature = "";

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject weatherPart = array.getJSONObject(i);
                            main = weatherPart.getString("main");
                            description = weatherPart.getString("description");
                        }

                        JSONObject mainPart = new JSONObject(mainTemperature);
                        temperature = mainPart.getString("temp");

                        visibility = Double.parseDouble(jsonObject.getString("visibility"));
                        //By default visibility is in meter
                        int visibilityInKilometer = (int) visibility / 1000;

                        Log.i("Temperature", temperature);

                        double t = Double.parseDouble(temperature) - 273.15;
                        t = Double.parseDouble(new DecimalFormat("##.##").format(t));


                        String resultText = "Main :                     " + main +
                                "\nDescription :        " + description +
                                "\nTemperature :        " + t + "*C" +
                                "\nVisibility :              " + visibilityInKilometer + " KM";
                            result.setText(resultText);


                        }
                        catch (JSONException e) {
                        e.printStackTrace();
                    }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.setText("That didn't work! prova ad inserire il nome di una città valido");
                //queue.cancelAll(this);
            }
        });
        //queue.add(stringRequest);
        MyVolley.getInstance(this).getQueue().add(stringRequest);
        hideKeyboard(this);
    }

                        //METODO ON_CREATE

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        EditText editText = (EditText) findViewById(R.id.Ab);
        editText.setText(message);
        result=findViewById(R.id.resut);
        result.setText("");


        ArrayList<String> c=new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line, arrayListLoad(c)
                //arrayListLoad(c) o forse new thead o UI therad
         );
        AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.Ab);
        auto.setAdapter(adapter);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


    }

    public void posizione(View view) {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PEMISSION
            );
        } else {
            getCurrentLocation();

            Intent intent = new Intent(this, Activity3.class);
            //TextView Text = (TextView) findViewById(R.id.resut);


            //String message = Text.getText().toString();
            //if(result.getText().toString().contains("lat=")||result.getText().toString()==null){
               if(w.contains("lat=")){
                String s = "meteo di 5 giorni";
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            intent.putExtra(EXTRA_MESSAGE, w);

            startActivity(intent);}else{
                String q= "POSIZIONE RILEVATA CORRETTAMENTE: tap sul bottone posizione un altra volta per il meteo di 5 giorni ";
                Toast.makeText(this, q, Toast.LENGTH_SHORT).show();
               result.setText("...");}

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PEMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();

            } else {
                Toast.makeText(this, "accesso", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void getCurrentLocation() {
        final TextView result = findViewById(R.id.resut);
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                LocationServices.getFusedLocationProviderClient(MainActivity.this).removeLocationUpdates(this);

                if(locationResult!=null && locationResult.getLocations().size()>0){
                    int latestLocationIndex=locationResult.getLocations().size()-1;
                    double latitude=locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude=locationResult.getLocations().get(latestLocationIndex).getLongitude();

                    //result.setText(String.format("lat=%s&lon=%s", latitude,longitude));
                    w=(String.format("lat=%s&lon=%s",latitude,longitude));

                }

            }
        }, Looper.getMainLooper());


    }

}
